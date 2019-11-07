package com.coswit.View;

import java.util.Locale;

/**
 * @author Created by zhengjing on 2019-11-06.
 */
public class YdTk2StatisticsBean {

    private int index;
    private StatisticBean statistic;

    public String getMonthDesc() {
        return String.format(Locale.CHINA, "%d月", index);
    }

    public String getWeekDesc() {
        return String.format(Locale.CHINA, "%d周", index);
    }

    public StatisticBean getStatistic() {
        return statistic;
    }

    public static class StatisticBean {

        private int questionCount;
        private int accuracy;
        private Object rank;
        private Object kbCount;
        private int duration;

        public int getQuestionCount() {
            return questionCount;
        }

        public int getAccuracy() {
            return accuracy;
        }

        public int getDuration() {
            return duration;
        }
    }
}
