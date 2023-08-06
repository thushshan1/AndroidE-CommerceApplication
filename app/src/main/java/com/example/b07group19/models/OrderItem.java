package com.example.b07group19.models;

import java.io.Serializable;

public  class OrderItem implements Serializable {


            private String productName;
            private String brand;
            private String price;
            private String purl;
            private String count;

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPurl() {
                return purl;
            }

            public void setPurl(String purl) {
                this.purl = purl;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }
        }
