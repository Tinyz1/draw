package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.LinkHitPrizeCache;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawLinkExample;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.DrawPrizeExample;
import com.asiainfo.draw.domain.LinkItem;
import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.LinkMemberExample;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.PrizeItem;
import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.persistence.DrawLinkMapper;
import com.asiainfo.draw.persistence.DrawPrizeMapper;
import com.asiainfo.draw.persistence.LinkMemberMapper;
import com.asiainfo.draw.persistence.ParticipantMapper;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.service.RecordService;
import com.asiainfo.draw.util.DefaultPrizePoolFactory;
import com.asiainfo.draw.util.ParticipantPrize;
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
	private LinkMemberMapper memberMapper;

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private ParticipantCache participantCache;

	@Autowired
	private RecordService recordService;

	@Autowired
	private LinkHitPrizeCache linkHitPrizeCache;

	@Autowired
	private CommandCache redirectCache;

	@Autowired
	private ParticipantMapper participantMapper;

	@Override
	public void initLink(Integer linkId) {

		try {
			logger.info("���ڳ�ʼ��->���Խ�����ǰ����...");
			finishCurrentLink();
		} catch (Exception e) {
			logger.warn(e.toString());
		}

		logger.info("<<===========��ȡ�µĻ���...");
		// ��ȡ��һ����
		DrawLink currentLink = linkMapper.selectByPrimaryKey(linkId);
		if (currentLink == null) {
			throw new RuntimeException("���ݻ���ID:" + linkId + "��ȡ�����齱����");
		}

		logger.info("���ڳ�ʼ��->��ǰ����:{}", currentLink.getLinkName());
		currentLinkCache.put(CurrentLinkCache.CURRENT_LINK, currentLink);

		logger.info("���ڳ�ʼ��->���ڲ�����Ա�б�Ĭ��Ϊ��.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, new ArrayList<Participant>());

		logger.info("���ڳ�ʼ��->ʣ��δ�齱��ԱΪ0.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_REMAIN_NUM, 0);

		logger.info("���ڳ�ʼ��->�����н���¼Ϊ��.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, new HashMap<Integer, DrawPrize>());

		logger.info("���ڳ�ʼ��->����ҡ����¼Ϊ��.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_SHAKE, new HashSet<Integer>());

		// �ճ�ʼ��ʱ����ǰ���ڲ��ܳ齱
		logger.info("���ڳ�ʼ��->����״̬����Ϊ��{}.", LinkState.INIT);
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.INIT);

		// ----------------------------------------------------------------------------
		// �޸����ݿ�Ļ���״̬Ϊ2(������)
		currentLink.setLinkState(2);
		linkMapper.updateByPrimaryKeySelective(currentLink);
	}

	/**
	 * ��ʼ����Ʒ�أ�һ����ѡ�˺�Ž��г�ʼ��
	 */
	@Override
	public void initPool() {

		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);

		logger.info("<<===========��ȡ�µĻ��ڽ�Ʒ...");
		List<DrawPrize> currentPrizes = getPrizeByLink(currentLink.getLinkId());

		logger.info("<<===========�ѵ�ǰ���ڽ�Ʒ���뻺����");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PRIZES, currentPrizes);

		// ��ʼ����ǰ���ڲ�����Ա
		LinkMemberExample memberExample = new LinkMemberExample();
		memberExample.createCriteria().andLinkIdEqualTo(currentLink.getLinkId()).andStateEqualTo(1);
		List<LinkMember> linkMembers = memberMapper.selectByExample(memberExample);
		if (linkMembers == null || linkMembers.size() == 0) {
			String mess = "�齱������Ա����Ϊ��";
			logger.info(mess);
			throw new RuntimeException(mess);
		}

		List<Participant> participants = new ArrayList<Participant>();
		for (LinkMember member : linkMembers) {
			Participant participant = participantCache.get(member.getParticipantId());
			participants.add(participant);
		}
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, participants);

		// ���Ѽ���������Ա״̬����Ϊ��ʹ��
		for (LinkMember member : linkMembers) {
			member.setState(2);
			memberMapper.updateByPrimaryKey(member);
		}

		logger.info("<<===========��ʼ����Ʒ��...");
		PrizePoolFactory poolFactory = new DefaultPrizePoolFactory();

		int numberOfPeople = participants.size();
		logger.info("<<===========���񻷽ڲ���������{}...", numberOfPeople);
		PrizePool pool = poolFactory.createPrizePools(numberOfPeople, currentPrizes);

		logger.info("<<===========�ѽ�Ʒ�ؼ��뻺����...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_POOL, pool);
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
			logger.error("��ǰ���ڲ����ڣ�");
		}
		if (currentLink != null) {
			finishLink(currentLink.getLinkId());
		}
	}

	@Override
	public void finishLink(Integer linkId) {
		// ���ڱ�־����Ϊ3(�ѽ���)
		DrawLink link = linkMapper.selectByPrimaryKey(linkId);
		link.setLinkState(3);
		linkMapper.updateByPrimaryKeySelective(link);

		logger.info(link.toString());

		logger.info("<<=========��������{}...", link.getLinkName());
		// �ѵ�ǰ���ڵĿ��عر�
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.FINISH);

		// �ѻ����н���¼д�����
		try {
			@SuppressWarnings("unchecked")
			Map<Integer, DrawPrize> currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			if (currentHits != null) {
				for (Map.Entry<Integer, DrawPrize> hit : currentHits.entrySet()) {
					WinningRecord winningRecord = new WinningRecord();
					// �н�����
					winningRecord.setLinkId(link.getLinkId());
					// �û�ID
					winningRecord.setParticipantId(hit.getKey());
					// ��ƷID
					winningRecord.setPrizeId(hit.getValue().getPrizeId());
					recordService.saveRecord(winningRecord);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		// ��յ�ǰ����
		currentLinkCache.invalidateAll();
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

	@Override
	public List<ParticipantPrize> getLinkHitPrize(Integer linkId) {
		checkNotNull(linkId);
		Map<String, DrawPrize> hitPrize = linkHitPrizeCache.get(linkId);
		if (hitPrize == null) {
			hitPrize = new HashMap<String, DrawPrize>();
		}

		List<ParticipantPrize> hitPrizes = new ArrayList<ParticipantPrize>();
		if (hitPrize != null) {
			for (Map.Entry<String, DrawPrize> hpriz : hitPrize.entrySet()) {
				ParticipantPrize ppr = new ParticipantPrize(getLinkByLinkId(linkId).getLinkName(), hpriz.getKey(), hpriz.getValue()
						.getPrizeType(), hpriz.getValue().getPrizeName());
				hitPrizes.add(ppr);
			}
		}
		return hitPrizes;
	}

	private DrawLink getLinkByLinkId(Integer linkId) {
		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		if (currentLink.getLinkId().intValue() == linkId.intValue()) {
			return currentLink;
		}
		return linkMapper.selectByPrimaryKey(linkId);
	}

	@Override
	public DrawLink getCurrentLink() {
		return (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
	}

	@Override
	public List<DrawLink> getAll() {
		DrawLinkExample linkExample = new DrawLinkExample();
		return linkMapper.selectByExample(linkExample);
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

		// ���ڽ������
		link.setEnterNumber(item.getEnterNumber());
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
