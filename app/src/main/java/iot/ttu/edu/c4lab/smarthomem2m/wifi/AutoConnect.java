package iot.ttu.edu.c4lab.smarthomem2m.wifi;

public class AutoConnect {

    //CCC;V+W/97S16#[/63U<&8G/# : c4:e9:2045
    int[] mac_location = {4, 6, 8, 10, 12, 14, 16, 18, 19, 20, 21, 22};
    int[] date_location = {3, 7, 11, 15, 5, 9, 13, 17};
    String mac = "";
    String date = "";
    String ssid = "";
    String pwd = "";
    int algo;

    public AutoConnect() {
        // TODO Auto-generated constructor stub
    }

    public AutoConnect(String ssid) {
        // TODO Auto-generated constructor stub
        this.ssid = ssid;
        checkalgo();
        genpwd();
    }

    public void start() {
        checkalgo();
        genpwd();
    }

    public void checkalgo() {
        if (ssid.startsWith("BBB")) {
            algo = 0;
        } else if (ssid.startsWith("CCC")) {
            algo = 1;
        }
    }

    public void genpwd() {
        byte[] aes_out = base64_d_algo(ssid.substring(3).getBytes());
        int[][] algo_pwd_location = {{1, 3, 5, 7, 0, 4, 8, 12, 2, 6, 10, 14}, {0, 2, 4, 6, 1, 5, 9, 13, 3, 7, 11, 15}};
        pwd += (char) aes_out[algo_pwd_location[algo][0]];
        pwd += (char) aes_out[algo_pwd_location[algo][1]];
        pwd += ":";
        pwd += (char) aes_out[algo_pwd_location[algo][2]];
        pwd += (char) aes_out[algo_pwd_location[algo][3]];
        pwd += ":";
        int pwdint = (aes_out[algo_pwd_location[algo][4]] - 48) * 1000 + (aes_out[algo_pwd_location[algo][5]] - 48) * 100
                + (aes_out[algo_pwd_location[algo][6]] - 48) * 10 + (aes_out[algo_pwd_location[algo][7]] - 48)
                + (aes_out[algo_pwd_location[algo][8]] - 48) * 10 + (aes_out[algo_pwd_location[algo][9]] - 48)
                + (aes_out[algo_pwd_location[algo][10]] - 48) * 10 + (aes_out[algo_pwd_location[algo][11]] - 48);
        pwd += String.valueOf(pwdint);
    }

    public byte[] base64_d_algo(byte[] aes_d_data) {
        int j = 0, ascii_num = 35;
        int number;
        byte[] aes_out = new byte[17];
        for (int i = 0; i < aes_d_data.length; i++) {
            if (aes_d_data[i] == 105) {
                aes_d_data[i] = 39;
            } else if (aes_d_data[i] == 106) {
                aes_d_data[i] = 96;
            } else if (aes_d_data[i] == 107) {
                aes_d_data[i] = 36;
            }
        }
        for (int i = 0; i < 17; i += 4) {
            number = (((aes_d_data[i] - ascii_num) << 18) + ((aes_d_data[i + 1] - ascii_num) << 12) + ((aes_d_data[i + 2] - ascii_num) << 6) + aes_d_data[i + 3] - ascii_num);
            aes_out[j] = (byte) (number >> 16);
            aes_out[j + 1] = (byte) ((number - (aes_out[j] << 16)) >> 8);
            aes_out[j + 2] = (byte) (number - ((aes_out[j] << 16) + (aes_out[j + 1] << 8)));
            j = j + 3;
        }
        aes_out[15] = (byte) (((aes_d_data[20] - ascii_num) << 2) + ((aes_d_data[21] - ascii_num) >> 4));
        aes_out[16] = 0;
        return aes_out;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public String getDate() {
        return date;
    }

    public String getMac() {
        return mac;
    }

    public String getPwd() {
        return pwd;
    }

    public int getAlgo() {
        return algo;
    }

//    public static void main(String[] args) {
//        autoconnect_algo3 ac = new autoconnect_algo3();
//        ac.setSsid("CCC;V+W/97S16#[/63U<&8G/#");
//        ac.start();
//        System.out.println(ac.getPwd());
//    }
}
