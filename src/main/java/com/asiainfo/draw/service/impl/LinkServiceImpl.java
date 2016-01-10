package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.DrawPrizeExample;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.persistence.DrawLinkMapper;
import com.asiainfo.draw.persistence.DrawPrizeMapper;
import com.asiainfo.draw.persistence.LinkMemberMapper;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.DefaultPrizePoolFactory;
import com.asiainfo.draw.util.PrizePool;
import com.asiainfo.draw.util.PrizePoolFactory;

@Service("linkService")
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

	@Override
	public void initNextLink() {
		// ��ȡ��һ����
		DrawLink currentLink = nextLink();
		if (currentLink == null) {
			throw new RuntimeException("û����һ���齱�����ˣ�");
		}
		logger.info("<<===========��ʼ�µĻ��ڣ�{}", currentLink.getLinkName());
		logger.info("<<===========�ѵ�ǰ���ڼ��뻺����...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_LINK, currentLink);

		List<DrawPrize> currentPrizes = getPrizeByLink(currentLink.getLinkId());
		logger.info("<<===========��ѯ����ǰ���ڵĽ�Ʒ��{}", currentPrizes);

		if (currentPrizes == null || currentPrizes.size() == 0) {
			throw new RuntimeException("��ǰ���ڲ���û�н�Ʒ��");
		}

		logger.info("<<===========�ѵ�ǰ����ʣ��Ľ�Ʒ���뻺����");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PRIZES, currentPrizes);

		List<Participant> participants = getParticipantByLink(currentLink.getLinkId());
		logger.info("<<===========��ѯ����ǰ���ڵĲ�����Ա��{}", participants);
		logger.info("<<===========�ѵ�ǰ���ڲ�����Ա���뻺����");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, participants);

		// ��ǰ�����н���Ա
		logger.info("<<===========��ʼ����ǰ�������н���Ա...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, new HashMap<Integer, DrawPrize>());

		// ��ʼ����Ʒ��
		PrizePoolFactory poolFactory = new DefaultPrizePoolFactory();
		logger.info("<<===========������ǰ���ڵĽ�Ʒ��...");

		int numberOfPeople = participantCache.getAll().size();
		if (participants != null && participants.size() > 0) {
			numberOfPeople = participants.size();
		}

		logger.info("<<===========���񻷽ڲ���������{}...", numberOfPeople);
		List<PrizePool> pools = poolFactory.createPrizePools(numberOfPeople, currentPrizes);

		logger.info("<<===========�ѽ�Ʒ�ؼ��뻺����...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_POOLS, pools);

		// �ճ�ʼ��ʱ����ǰ���ڲ��ܳ齱
		logger.info("<<===========��ʼ������״̬Ϊ��{}...", LinkState.INIT);
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.INIT);
	}

	/**
	 * ��ȡ��ǰ�����ܹ��������Ա��
	 * 
	 * @param linkId
	 *            ����ID
	 * @return ������Ա�б�
	 */
	private List<Participant> getParticipantByLink(Integer linkId) {
		checkNotNull(linkId);
		List<Participant> participants = memberMapper.selectParticipantByLink(linkId);
		return participants;
	}

	@Override
	public DrawLink nextLink() {
		// ����˳����С��δ��ʼ��Ϊ��һ����ʼ����
		DrawLink nextLink = linkMapper.selectNextLink();
		logger.info("<<==��һ���ڣ�" + nextLink);
		return nextLink;
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

	@Override
	public void finishCurrentLink() {

		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		logger.info("<<===========��ǰ���ڣ�{}��ʼ����...", currentLink.getLinkName());

		logger.info("<<===========���õ�ǰ���ڵ�״̬Ϊ��{}", LinkState.FINISH);
		// �ѵ�ǰ���ڵĿ��ش�
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.FINISH);
		// ��¼���ڿ�ʼʱ��
		currentLinkCache.put(CurrentLinkCache.CURRENT_FINISH_DATE, new Date());
		// �ѻ����н���¼д�����

		// ��յ�ǰ����
		currentLinkCache.clear();
		// ��ʼ����һ������
		initNextLink();
	}

	@Override
	public void startCurrentLink() {
		logger.info("<<========���������µĳ齱����...");
		// ��ǰ���ڻ�û�н���ʱ�����������µĻ���
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		logger.info("<<=============��ǰ����״̬��{}" + linkState);
		if (linkState == LinkState.RUN) {
			String msg = "��ǰ�����Ѿ���������״̬������ʧ��...";
			logger.info(msg);
			throw new StartLinkException(msg);
		}
		if (linkState == LinkState.FINISH) {
			String msg = "�µĻ���δ��ʼ��������ʧ��...";
			logger.info(msg);
			throw new StartLinkException(msg);
		}
		logger.info("<<==========�ѵ�ǰ���ڵ�״̬����Ϊ��{}" + LinkState.RUN);
		// �ѵ�ǰ���ڵĿ��ش�
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.RUN);
		Date start = new Date();
		logger.info("����µĻ�������������ʱ�䣺{}" + start);
		// ��¼���ڿ�ʼʱ��
		currentLinkCache.put(CurrentLinkCache.CURRENT_START_DATE, start);
	}

}
