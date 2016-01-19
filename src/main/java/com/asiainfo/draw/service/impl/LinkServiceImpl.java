package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import com.asiainfo.draw.util.Command;
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
	private ParticipantMapper participantMapper;

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

		logger.info("���ڳ�ʼ��->���ڲ�����Ա�б�Ĭ��Ϊ��.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, new ArrayList<Participant>());

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

		// ��ʼʱ������������
		commandCache.put(CommandCache.CURRENT_COMMAND, Command.redirect("screen.jsp"));
	}

	/**
	 * ��ʼ����Ʒ�أ�һ����ѡ�˺�Ž��г�ʼ��
	 */
	@Override
	public void initPool() {

		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);

		logger.info("<<===========��ȡ�µĻ��ڽ�Ʒ...");
		List<DrawPrize> currentPrizes = getPrizeByLink(currentLink.getLinkId());
		if (currentPrizes == null || currentPrizes.size() == 0) {
			throw new NullPointerException("����:" + currentLink.getLinkName() + "�Ľ�Ʒû�����ã�");
		}

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

		int numberOfPerson = linkMembers.size();
		logger.info(">>��ǰ������Ա����:{}", numberOfPerson);

		List<Participant> participants = new ArrayList<Participant>();
		for (LinkMember member : linkMembers) {
			// ���Ѽ���������Ա״̬����Ϊ��ʹ��
			member.setState(2);
			memberMapper.updateByPrimaryKey(member);

			// �û��ĳ齱����-1
			Participant participant = participantCache.get(member.getParticipantId());
			participant.setState(participant.getState() - 1 >= 0 ? participant.getState() - 1 : participant.getState());
			participantMapper.updateByPrimaryKeySelective(participant);

			participants.add(participant);
		}
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, participants);

		int numberOfPrize = 0;
		for (DrawPrize prize : currentPrizes) {
			numberOfPrize += prize.getSize();
		}
		logger.info(">>��ǰ�������õĽ�Ʒ����:{}", numberOfPrize);

		// ��������������ڽ�Ʒ��
		if (numberOfPerson > numberOfPrize) {
			try {
				DrawPrize defaultPrize = getPrizeByLink(0).get(0);
				defaultPrize.setSize(numberOfPerson - numberOfPrize);
				currentPrizes.add(defaultPrize);
				logger.info(">>��ǰ���ڲ�����Ա�����������õĽ�Ʒ����������Ĭ�ϵĽ�Ʒ����:{}", numberOfPerson - numberOfPrize);
			} catch (Exception e) {
				logger.error(">>û������Ĭ�ϻ��ڽ�Ʒ��Ĭ�ϻ��ڽ�Ʒ�Ļ���IDΪ0��ֻ������һ�����ݡ�");
			}
		}

		logger.info("<<===========��ʼ����Ʒ��...");
		PrizePoolFactory poolFactory = new DefaultPrizePoolFactory();

		int numberOfPeople = linkMembers.size();
		logger.info("<<===========���񻷽ڲ���������{}...", numberOfPeople);
		PrizePool pool = poolFactory.createPrizePools(currentPrizes);

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
	@SuppressWarnings("unchecked")
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
			Map<Integer, DrawPrize> currentHits = null;
			try {
				currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			} catch (Exception e) {
				logger.warn(">>������Ϣ��{}", e);
			}
			// ��¼�����н���¼
			if (currentHits != null && currentHits.size() > 0) {
				List<WinningRecord> records = new ArrayList<WinningRecord>();
				for (Map.Entry<Integer, DrawPrize> hit : currentHits.entrySet()) {

					WinningRecord winningRecord = new WinningRecord();
					// �н�����
					winningRecord.setLinkId(currentLink.getLinkId());
					// �û�ID
					winningRecord.setParticipantId(hit.getKey());
					// ��ƷID
					winningRecord.setPrizeId(hit.getValue().getPrizeId());
					records.add(winningRecord);
				}
				logger.info(">>��¼��ǰ�����н���¼...");
				recordService.saveRecord(records);
			}
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
	@SuppressWarnings("unchecked")
	@Override
	public List<ParticipantPrize> getCurrnetLinkHitPrize() {

		List<ParticipantPrize> participantPrizes = new ArrayList<ParticipantPrize>();

		// ��ǰ����
		DrawLink currentLink = null;
		try {
			currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.info("��ǰ�����ѽ�����");
		}

		// ��ǰ���ڽ�����ʱ�����б�Ҫ���ص�ǰ���ڵ��н���¼
		if (currentLink != null) {
			// �����н���¼
			Map<Integer, DrawPrize> hits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);

			for (Map.Entry<Integer, DrawPrize> hit : hits.entrySet()) {
				// ������Ա
				Participant participant = participantCache.get(hit.getKey());
				// ��Ʒ
				DrawPrize prize = hit.getValue();
				ParticipantPrize participantPrize = new ParticipantPrize(currentLink.getLinkName(),
						participant.getParticipantName(), prize.getPrizeType(), prize.getPrizeName());
				// �����б�
				participantPrizes.add(participantPrize);
			}
		}

		return participantPrizes;
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
