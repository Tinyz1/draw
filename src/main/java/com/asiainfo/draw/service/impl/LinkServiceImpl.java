package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawLinkExample;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.DrawPrizeExample;
import com.asiainfo.draw.domain.LinkItem;
import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.PrizeItem;
import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.persistence.DrawLinkMapper;
import com.asiainfo.draw.persistence.DrawPrizeMapper;
import com.asiainfo.draw.service.LinkMemberService;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.service.ParticipantService;
import com.asiainfo.draw.service.RecordService;
import com.asiainfo.draw.util.Command;
import com.asiainfo.draw.util.DefaultPrizePoolFactory;
import com.asiainfo.draw.util.PrizePool;
import com.asiainfo.draw.util.PrizePoolFactory;

@Service("linkService")
@Transactional
public class LinkServiceImpl implements LinkService {

	private final Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);

	@Autowired
	private DrawLinkMapper linkMapper;

	@Autowired
	private DrawPrizeMapper prizeMapper;

	@Autowired
	private LinkMemberService memberService;

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private ParticipantCache participantCache;

	@Autowired
	private RecordService recordService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private CommandCache commandCache;

	@Override
	public void initLink(Integer linkId) {

		logger.info("���ڳ�ʼ��->���Խ�����ǰ����...");
		finishCurrentLink();
		logger.info(">>��յ�ǰ���ڻ���...");
		currentLinkCache.invalidateAll();

		logger.info("<<===========��ȡ�µĻ���...");
		// ��ȡ��һ����
		DrawLink currentLink = linkMapper.selectByPrimaryKey(linkId);
		if (currentLink == null) {
			throw new RuntimeException("���ݻ���ID:" + linkId + "��ȡ�����齱����");
		}

		logger.info("���ڳ�ʼ��->��ǰ����:{}", currentLink.getLinkName());
		currentLinkCache.put(CurrentLinkCache.CURRENT_LINK, currentLink);

		// �ճ�ʼ��ʱ����ǰ���ڲ��ܳ齱
		logger.info("���ڳ�ʼ��->����״̬����Ϊ��{}.", LinkState.INIT);
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.INIT);

		// ----------------------------------------------------------------------------
		// �޸����ݿ�Ļ���״̬Ϊ2(������)
		currentLink.setLinkState(2);
		linkMapper.updateByPrimaryKeySelective(currentLink);

		// ��ʼʱ������������
		commandCache.put(CommandCache.CURRENT_COMMAND, Command.redirect("screen.jsp"));
	}

	/**
	 * ��ʼ����Ʒ�أ�һ����ѡ�˺�Ž��г�ʼ��
	 */
	@Override
	public void initPool() {

		DrawLink link = null;
		try {
			link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.info(">>��ǰ���ڲ����ڣ���ϸ��Ϣ:{}", e);
		}
		if (link != null) {

			List<DrawPrize> prizes = getPrizeByLink(link.getLinkId());
			if (prizes == null || prizes.size() == 0) {
				logger.warn("����:{}û�����ý�Ʒ��", link.getLinkName());
				prizes = new ArrayList<DrawPrize>();
			}
			int numberOfPrize = 0;
			for (DrawPrize prize : prizes) {
				if (prize != null) {
					numberOfPrize += prize.getSize();
				}
			}
			logger.info(">>��ǰ�������õĽ�Ʒ����:{}", numberOfPrize);

			/* ȷ�ϳ齱�û� */
			List<LinkMember> members = memberService.getMemberByLinkIdAndState(link.getLinkId(), LinkMember.STATE_CREATE);
			if (members == null || members.size() == 0) {
				throw new RuntimeException("�齱������Ա����Ϊ��");
			}
			int numberOfPerson = members.size();
			logger.info(">>��ǰ������Ա����:{}", numberOfPerson);
			memberService.confirm(members);

			/* �齱�������1�� */
			List<Participant> participants = new ArrayList<Participant>();
			for (LinkMember member : members) {
				participants.add(participantCache.get(member.getParticipantId()));
			}
			participantService.subShakeTime(participants);

			// ��������������ڽ�Ʒ��
			if (numberOfPerson > numberOfPrize) {
				try {
					DrawPrize prize = getPrizeByLink(0).get(0);
					prize.setSize(numberOfPerson - numberOfPrize);
					prizes.add(prize);
					logger.info(">>��ǰ���ڲ�����Ա�����������õĽ�Ʒ����������Ĭ�ϵĽ�Ʒ����:{}", numberOfPerson - numberOfPrize);
				} catch (Exception e) {
					logger.error(">>û������Ĭ�ϻ��ڽ�Ʒ��Ĭ�ϻ��ڽ�Ʒ�Ļ���IDΪ0��ֻ������һ�����ݡ�");
				}
			}

			logger.info("<<===========��ʼ����Ʒ��...");
			PrizePoolFactory poolFactory = new DefaultPrizePoolFactory();
			PrizePool pool = poolFactory.createPrizePools(prizes);

			logger.info("<<===========�ѽ�Ʒ�ؼ��뻺����...");
			currentLinkCache.put(CurrentLinkCache.CURRENT_POOL, pool);
		}
	}

	/**
	 * ���ݻ���ID��ѯ���ڽ�Ʒ
	 * 
	 * @param linkId
	 *            ����ID
	 * @return ��ǰ���ڵ����н�Ʒ
	 */
	private List<DrawPrize> getPrizeByLink(Integer linkId) {
		checkNotNull(linkId);
		DrawPrizeExample prizeExample = new DrawPrizeExample();
		// ����ID���
		prizeExample.createCriteria().andLinkIdEqualTo(linkId);
		List<DrawPrize> prizes = prizeMapper.selectByExample(prizeExample);
		logger.debug("<<==��ѯ������linkId->{}����ѯ�����{}", linkId, prizes);
		return prizes;
	}

	/**
	 * ������ǰ����
	 */
	private void finishCurrentLink() {
		DrawLink currentLink = null;
		try {
			currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.error(">>�����ѽ�����");
		}
		if (currentLink != null) {
			// �ѵ�ǰ���ڵĿ��عر�
			currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.FINISH);
			// ��յ�ǰ����
			currentLinkCache.invalidateAll();
			// ����״̬����Ϊ�ѽ���
			finishLink(currentLink.getLinkId());
		}
	}

	@Override
	public void finishLink(Integer linkId) {
		checkNotNull(linkId);
		DrawLink link = linkMapper.selectByPrimaryKey(linkId);
		link.setLinkState(3); // ���ڱ�־����Ϊ3(�ѽ���)
		linkMapper.updateByPrimaryKeySelective(link);
		DrawLink currentLink = null;
		try {
			currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.error("��ǰ���ڲ����ڣ�");
		}
		// ���ڵ�ǰ���ڣ����ҽ�ϻ���Ϊ��ǰ����ʱ���ѵ�ǰ���ڽ���
		if (currentLink != null && linkId.equals(currentLink.getLinkId())) {
			finishCurrentLink();
		}
	}

	@Override
	public void startCurrentLink() {
		logger.info("<<========���������µĳ齱����...");
		// ��ǰ���ڻ�û�н���ʱ�����������µĻ���
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		logger.info("<<=============��ǰ����״̬:{}", linkState);
		if (linkState == LinkState.RUN) {
			String msg = "��ǰ�����Ѿ���������״̬������ʧ��...";
			logger.info(msg);
			throw new StartLinkException(msg);
		} else if (linkState == LinkState.FINISH) {
			String msg = "�µĻ���δ��ʼ��������ʧ��...";
			logger.info(msg);
			throw new StartLinkException(msg);
		} else if (linkState == LinkState.INIT) {
			logger.info("���ڿ�ʼ->�ѵ�ǰ���ڵ�״̬����Ϊ:{}", LinkState.RUN);
			// �ѵ�ǰ���ڵĿ��ش�
			currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.RUN);
		}
	}

	/**
	 * ��ȡ��ǰ�����û��н���¼
	 * 
	 * @return
	 */
	@Override
	public List<WinningRecord> getCurrnetLinkHitPrize() {
		// ��ǰ����
		DrawLink currentLink = null;
		try {
			currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.info("��ǰ�����ѽ�����");
		}
		List<WinningRecord> records = new ArrayList<WinningRecord>();
		// ��ǰ���ڽ�����ʱ�����б�Ҫ���ص�ǰ���ڵ��н���¼
		if (currentLink != null) {
			// �����н���¼
			records = recordService.getRecordByParticipantNameAndLinkId(null, currentLink.getLinkId());
		}
		return records;
	}

	@Override
	public DrawLink getCurrentLink() {
		return (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
	}

	@Override
	public List<DrawLink> getAll() {
		DrawLinkExample linkExample = new DrawLinkExample();
		List<DrawLink> links = linkMapper.selectByExample(linkExample);
		logger.info(links.toString());
		return links;
	}

	@Override
	public void resetLink(Integer linkId) {
		// ���û�����ζ�ţ����Ѿ������Ļ���״̬��Ϊ1��δ��ʼ��
		DrawLink link = linkMapper.selectByPrimaryKey(linkId);
		if (link == null) {
			throw new NullPointerException("����Ϊ��");
		}
		link.setLinkState(1);
		linkMapper.updateByPrimaryKeySelective(link);
	}

	@Override
	public void add(LinkItem item) {
		// �����齱����
		DrawLink link = new DrawLink();
		link.setLinkName(item.getLinkName());
		// ֻ��δ�н����˿���
		link.setOpenState(1);
		// δ��ʼ״̬
		link.setLinkState(1);

		link.setLinkOrder(0);

		String enterNumber = item.getEnterNumber();
		// ������֤��Ϊ��ʱ��Ĭ������Ϊ123456
		if (StringUtils.isBlank(item.getEnterNumber())) {
			enterNumber = "123456";
		}
		// ���ڽ������
		link.setEnterNumber(enterNumber);
		linkMapper.insert(link);

		// Ŀ�ģ�Ϊ�˻�ȡ�������ݵ�ID
		DrawLinkExample linkExample = new DrawLinkExample();
		linkExample.createCriteria().andLinkNameEqualTo(item.getLinkName());
		link = linkMapper.selectByExample(linkExample).get(0);

		// ������Ʒ
		List<PrizeItem> prizeItems = item.getPrizeItems();
		if (prizeItems != null && prizeItems.size() > 0) {
			for (PrizeItem prizeItem : prizeItems) {
				DrawPrize prize = new DrawPrize();
				prize.setLinkId(link.getLinkId());
				prize.setPrizeName(prizeItem.getPrizeName());
				prize.setPrizeType(prizeItem.getPrizeType());
				prize.setSize(prizeItem.getSize());
				prizeMapper.insert(prize);
			}
		}

	}

}
