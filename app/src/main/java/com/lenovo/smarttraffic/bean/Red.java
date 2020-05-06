package com.lenovo.smarttraffic.bean;

import java.util.List;

public class Red {

    private List<RedBean> red;

    public List<RedBean> getRed() {
        return red;
    }

    public void setRed(List<RedBean> red) {
        this.red = red;
    }

    public static class RedBean {
        /**
         * red : 10
         * green : 10
         * yellow : 10
         */

        private int red;
        private int green;
        private int yellow;

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getYellow() {
            return yellow;
        }

        public void setYellow(int yellow) {
            this.yellow = yellow;
        }
    }
}
