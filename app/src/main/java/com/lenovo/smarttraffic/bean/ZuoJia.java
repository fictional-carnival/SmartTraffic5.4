package com.lenovo.smarttraffic.bean;

import java.util.List;

public class ZuoJia {

    private List<CarInfoBean> carInfo;
    private List<HistoryBean> history;

    public List<CarInfoBean> getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(List<CarInfoBean> carInfo) {
        this.carInfo = carInfo;
    }

    public List<HistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryBean> history) {
        this.history = history;
    }

    public static class CarInfoBean {
        public CarInfoBean(int name, int money, int status) {
            this.name = name;
            this.money = money;
            this.status = status;
        }

        /**
         * name : 1
         * money : 100
         * status : 0
         */

        private int name;
        private int money;
        private int status;

        public int getName() {
            return name;
        }

        public void setName(int name) {
            this.name = name;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class HistoryBean {
        /**
         * name : 1
         * money : 50
         * datetime : 2020-05-04 11:18:01
         */

        private int name;
        private int money;
        private String datetime;

        public HistoryBean(int name, int money, String datetime) {
            this.name = name;
            this.money = money;
            this.datetime = datetime;
        }

        public int getName() {
            return name;
        }

        public void setName(int name) {
            this.name = name;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }
    }
}
