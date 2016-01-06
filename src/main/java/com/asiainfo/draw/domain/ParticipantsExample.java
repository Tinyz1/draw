package com.asiainfo.draw.domain;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ParticipantsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andParticipantIdIsNull() {
            addCriterion("participant_id is null");
            return (Criteria) this;
        }

        public Criteria andParticipantIdIsNotNull() {
            addCriterion("participant_id is not null");
            return (Criteria) this;
        }

        public Criteria andParticipantIdEqualTo(Integer value) {
            addCriterion("participant_id =", value, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdNotEqualTo(Integer value) {
            addCriterion("participant_id <>", value, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdGreaterThan(Integer value) {
            addCriterion("participant_id >", value, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("participant_id >=", value, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdLessThan(Integer value) {
            addCriterion("participant_id <", value, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdLessThanOrEqualTo(Integer value) {
            addCriterion("participant_id <=", value, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdIn(List<Integer> values) {
            addCriterion("participant_id in", values, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdNotIn(List<Integer> values) {
            addCriterion("participant_id not in", values, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdBetween(Integer value1, Integer value2) {
            addCriterion("participant_id between", value1, value2, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantIdNotBetween(Integer value1, Integer value2) {
            addCriterion("participant_id not between", value1, value2, "participantId");
            return (Criteria) this;
        }

        public Criteria andParticipantNameIsNull() {
            addCriterion("participant_name is null");
            return (Criteria) this;
        }

        public Criteria andParticipantNameIsNotNull() {
            addCriterion("participant_name is not null");
            return (Criteria) this;
        }

        public Criteria andParticipantNameEqualTo(String value) {
            addCriterion("participant_name =", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameNotEqualTo(String value) {
            addCriterion("participant_name <>", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameGreaterThan(String value) {
            addCriterion("participant_name >", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameGreaterThanOrEqualTo(String value) {
            addCriterion("participant_name >=", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameLessThan(String value) {
            addCriterion("participant_name <", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameLessThanOrEqualTo(String value) {
            addCriterion("participant_name <=", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameLike(String value) {
            addCriterion("participant_name like", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameNotLike(String value) {
            addCriterion("participant_name not like", value, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameIn(List<String> values) {
            addCriterion("participant_name in", values, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameNotIn(List<String> values) {
            addCriterion("participant_name not in", values, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameBetween(String value1, String value2) {
            addCriterion("participant_name between", value1, value2, "participantName");
            return (Criteria) this;
        }

        public Criteria andParticipantNameNotBetween(String value1, String value2) {
            addCriterion("participant_name not between", value1, value2, "participantName");
            return (Criteria) this;
        }

        public Criteria andTelphoneIsNull() {
            addCriterion("telphone is null");
            return (Criteria) this;
        }

        public Criteria andTelphoneIsNotNull() {
            addCriterion("telphone is not null");
            return (Criteria) this;
        }

        public Criteria andTelphoneEqualTo(String value) {
            addCriterion("telphone =", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotEqualTo(String value) {
            addCriterion("telphone <>", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneGreaterThan(String value) {
            addCriterion("telphone >", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneGreaterThanOrEqualTo(String value) {
            addCriterion("telphone >=", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLessThan(String value) {
            addCriterion("telphone <", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLessThanOrEqualTo(String value) {
            addCriterion("telphone <=", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLike(String value) {
            addCriterion("telphone like", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotLike(String value) {
            addCriterion("telphone not like", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneIn(List<String> values) {
            addCriterion("telphone in", values, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotIn(List<String> values) {
            addCriterion("telphone not in", values, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneBetween(String value1, String value2) {
            addCriterion("telphone between", value1, value2, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotBetween(String value1, String value2) {
            addCriterion("telphone not between", value1, value2, "telphone");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberIsNull() {
            addCriterion("participant_number is null");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberIsNotNull() {
            addCriterion("participant_number is not null");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberEqualTo(String value) {
            addCriterion("participant_number =", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberNotEqualTo(String value) {
            addCriterion("participant_number <>", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberGreaterThan(String value) {
            addCriterion("participant_number >", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberGreaterThanOrEqualTo(String value) {
            addCriterion("participant_number >=", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberLessThan(String value) {
            addCriterion("participant_number <", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberLessThanOrEqualTo(String value) {
            addCriterion("participant_number <=", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberLike(String value) {
            addCriterion("participant_number like", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberNotLike(String value) {
            addCriterion("participant_number not like", value, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberIn(List<String> values) {
            addCriterion("participant_number in", values, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberNotIn(List<String> values) {
            addCriterion("participant_number not in", values, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberBetween(String value1, String value2) {
            addCriterion("participant_number between", value1, value2, "participantNumber");
            return (Criteria) this;
        }

        public Criteria andParticipantNumberNotBetween(String value1, String value2) {
            addCriterion("participant_number not between", value1, value2, "participantNumber");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}