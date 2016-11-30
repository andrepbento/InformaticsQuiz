package interfaces;

/**
 * Created by andre
 */

public interface PublicConstantValues {
    // GAME CONFIG
    int SP_MODE = 0;
    int MP_MODE = 1;
    int MAX_N_QUESTIONS = 15;
    int MAX_N_PLAYERS = 4;

    // GAME
    int tickTime = 1000;
    Integer[] easyGameValues = {60, 30, 10};
    Integer[] moderateGameValues = {30, 40, 30};
    Integer[] hardGameValues = {10, 30, 60};
    Integer easyGameTime = 90; // seconds
    Integer moderateGameTime = 60;
    Integer hardGameTime = 30;

    // PLAYER
    String playerFileName = "playerData.bin";

    // CAMERA
    int PROFILE_PHOTO = 0;
    int QRCODE_PHOTO = 1;

    // NETWORK
    int listeningPort = 9800;

    // VIBRATOR
    long VIBRATION_SHORT = 500;
    long VIBRATION_MEDIUM = 1000;
    long VIBRATION_LONG = 1500;
}
