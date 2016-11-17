package interfaces;

/**
 * Created by andre on 10/11/2016.
 */

public interface PublicConstantValues {

    // GAME CONFIG
    int SP_MODE = 0;
    int MP_MODE = 1;

    // GAME
    int tickTime = 1000;
    int MAX_GAME_N_QUESTIONS_SIZE = 15;

    // PLAYER
    String playerFileName = "playerData.bin";

    // CAMERA CONFIG
    int CAMERA_BACK = 0;
    int CAMERA_FRONT = 1;

    int PROFILE_PHOTO = 2;
    int QRCODE_PHOTO = 3;

    // NETWORK
    int listeningPort = 9800;
}
