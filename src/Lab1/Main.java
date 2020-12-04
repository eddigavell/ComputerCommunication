package Lab1;

public class Main {

    public static void main(String[] args) {
        byte [] buf = {  36,   1,  0, -25,
                          0,   0,  0,   0,
                          0,   0,  0,   2,
                         80,  80, 83,   0,
                        -29, 116,  5,  61,  0,  0,    0,   0,
                        -29, 116,  5,  59, 14, 86,    0,   0,
                        -29, 116,  5,  62,  0, 47, -121, -38,
                        -29, 116,  5,  62,  0, 47, -113,  -1};
        SNTPMessage msg = new SNTPMessage(buf);
    }
}
