package Lab1;

public class SNTPMessage {
    /*
                        1                   2                   3
       0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9  0  1
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |LI | VN  |Mode |    Stratum    |     Poll      |   Precision    |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                          Root  Delay                           |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                       Root  Dispersion                         |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                     Reference Identifier                       |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                                                                |
      |                    Reference Timestamp (64)                    |
      |                                                                |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                                                                |
      |                    Originate Timestamp (64)                    |
      |                                                                |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                                                                |
      |                     Receive Timestamp (64)                     |
      |                                                                |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                                                                |
      |                     Transmit Timestamp (64)                    |
      |                                                                |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                 Key Identifier (optional) (32)                 |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
      |                                                                |
      |                                                                |
      |                 Message Digest (optional) (128)                |
      |                                                                |
      |                                                                |
      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */
    /* Exempel på SNTP message
    byte [] buf = {  36,   1,  0, -25,
                      0,   0,  0,   0,
                      0,   0,  0,   2,
                     80,  80, 83,   0,
                    -29, 116,  5,  61,  0,  0,    0,   0,
                    -29, 116,  5,  59, 14, 86,    0,   0,
                    -29, 116,  5,  62,  0, 47, -121, -38,
                    -29, 116,  5,  62,  0, 47, -113,  -1}
    */

    private byte leapIndicator = 0;
    private byte versionNumber = 4;
    private byte mode = 0;
    private short stratum = 0;
    private short pollInterval = 0;
    private byte precision = 0;
    /*Första byten 36
     | 0 1 | 2 3 4 | 5 6 7 |
     | LI  |  VN   |  Mode |
       0 0   1 0 0   1 0 0 - (00100100) byte kod
        0      4       4
    */

    private double rootDelay = 0;
    private double rootDispersion = 0;
    /*
    rootDelay 32-bit signed fixed-point number
    fraction point between 14 and 16

    0000 0000 0000 0000 . 0000 0000 0000 0000

    Same for rootDispersion
     */

    private byte[] referenceIdentifier = {0, 0, 0, 0};
    /* Reference identifier 32-bits
    80,  80, 83,   0,
     P,   P,  S,   0
    */

    private double referenceTimeStamp = 0;
    private double originateTimeStamp = 0;
    private double receiveTimeStamp = 0;
    private double transmitTimeStamp = 0;

    SNTPMessage(byte[] buf) {
        byte b = buf[0];
        /* b = 36
        | 0 1 | 2 3 4 | 5 6 7 |
        | LI  |  VN   |  Mode |
          0 0   1 0 0   1 0 0 - (00100100) byte kod
           0      4       4
        */

        leapIndicator = (byte) ((b>>6) & 0x3);
        //00100100 -> 0010 0100
        // >> skiftar alla bits 6 steg till höger
        //0001 0010 b>>1
        //0000 1001 b>>2 osv... osv...
        //0000 0000 Resultat av b>>6
        //0x3? -> 0011
        // 36decimalt -> 24hex -> 0010 0100 binärt
        versionNumber = (byte) ((b>>3) & 0x7);
        //skifta 3 steg till höger
        // 0010 0100 -> 0000 0100
        // & 0x7?
        //0000 0100 -> 0000 0111
        mode = (byte) (b & 0x7); //0010 0100
        stratum = buf[1]; // 1
        pollInterval = buf[2]; // 0
        precision = buf[3]; // -25
        rootDelay = buf[4] + buf[5] + buf[6] + buf[7]; // 0,   0,  0,   0,
        rootDispersion = buf[8] + buf[9] + buf[10] + buf[11]; // 0,   0,  0,   2,
        referenceIdentifier[0] = buf[12]; // referenceIdentifier = 80, 80, 83, 0,
        referenceIdentifier[1] = buf[13];
        referenceIdentifier[2] = buf[14];
        referenceIdentifier[3] = buf[15];

        referenceTimeStamp = buf[16] + buf[17] + buf[18] + buf[19] + buf[20] + buf[21] + buf[22] + buf[23]; // -29, 116,  5,  61,  0,  0,    0,   0,
        originateTimeStamp = buf[24] + buf[25] + buf[26] + buf[27] + buf[28] + buf[29] + buf[30] + buf[31]; // -29, 116,  5,  59, 14, 86,    0,   0,
        receiveTimeStamp = buf[32] + buf[33] + buf[34] + buf[35] + buf[36] + buf[37] + buf[38] + buf[39];   // -29, 116,  5,  62,  0, 47, -121, -38,
        transmitTimeStamp = buf[40] + buf[41] + buf[42] + buf[43] + buf[44] + buf[45] + buf[46] + buf[47];  // -29, 116,  5,  62,  0, 47, -113,  -1}
        /* Reference TimeStamp, Originate TimeStamp, Receive TimeStamp, Transmit TimeStamp:
        those fields of 64 bits contain the four informations of time in connection with the “delays” and “the offset”.
        The first 32 bits indicate the number of seconds passed since January 1, 1900 to 0h00 UTC.
        The last 32 bits indicate the 1/232 seconds number spent since the beginning of the current second.
        In Broadcast mode, the fields Originate TimeStamp and Receive TimeStamp are by convention set to zero.
        TimeStamp reference: time of the last update of the clock
        Originate TimeStamp: time of departure of the request.
        Receive TimeStamp: time of arrival of the request
        TimeStamp transmitted: time of departure of the response to the request.
        Authenticator (optional): this fields is kept for the mechanism of authentication of NTP messages.
        This mechanism authorizes the encoding of NTP data with the DES (Data Encryption Standard).
        It is packed up in the appendix C of the RFC 1305.
         */



        0001 0000

           128 64 32 16     8 4 2 1
            0   0  0  0     0 0 1 1
    }
}
