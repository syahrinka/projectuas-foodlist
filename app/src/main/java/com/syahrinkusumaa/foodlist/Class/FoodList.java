package com.syahrinkusumaa.foodlist.Class;

public class FoodList {
        private String id_foodlist, nama_makanan, waktu, keterangan, img;


        public FoodList(String id_foodlist, String nama_makanan, String waktu, String keterangan, String img) {
            this.id_foodlist = id_foodlist;
            this.nama_makanan = nama_makanan;
            this.waktu = waktu;
            this.keterangan = keterangan;
            this.img = img;
        }

        public FoodList(String nama_makanan, String waktu, String keterangan, String img) {
            this.nama_makanan = nama_makanan;
            this.waktu = waktu;
            this.keterangan = keterangan;
            this.img = img;
        }

        public FoodList() {
        }

        public String getId_foodlist() {
            return id_foodlist;
        }

        public void setId_foodlist(String id_foodlist) {
            this.id_foodlist = id_foodlist;
        }

        public String getnama_makanan() {
            return nama_makanan;
        }

        public void setnama_makanan(String nama_makanan) {
            this.nama_makanan = nama_makanan;
        }

        public String getWaktu() {
            return waktu;
        }

        public void setWaktu(String waktu) {
            this.waktu = waktu;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan) {
            this.keterangan = keterangan;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

