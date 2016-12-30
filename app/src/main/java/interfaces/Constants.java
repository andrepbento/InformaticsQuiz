package interfaces;

/**
 * Created by andre
 */

public interface Constants {
    long DAY_IN_MS = 1000 * 60 * 60 * 24;

    // GAME CONFIG
    int SP_MODE = 0;
    int MP_MODE = 1;
    int MAX_N_QUESTIONS = 20;
    int MAX_N_PLAYERS = 16;

    // GAME
    int tickTime = 1000;
    int timeToNextQuestion = 1500;
    Integer[] easyGameValues = {60, 30, 10};
    Integer[] moderateGameValues = {30, 40, 30};
    Integer[] hardGameValues = {10, 30, 60};
    Integer easyGameTime = 90;
    Integer moderateGameTime = 60;
    Integer hardGameTime = 30;

    // STORAGE
    String playerFileName = "playerData.bin";
    String singlePlayerPath = "singlePlayerGames";
    String multiPlayerPath = "multiPlayerGames";

    // CAMERA
    int PROFILE_PHOTO = 1;
    int QRCODE_PHOTO = 2;

    // BITMAP CONFIG
    int BITMAP_HEIGHT_LARGE = 640;
    int BITMAP_WIDHT_LARGE = 480;

    // NETWORK
    int serverListeningPort = 9800;

    // VIBRATOR
    long VIBRATION_SHORT = 500;
    long VIBRATION_MEDIUM = 1000;
    long VIBRATION_LONG = 1500;

    // MSG_CODES
    int MSG_CODE_PLAYER_DATA = 1;
    int MSG_CODE_GAME = 2;
    int MSG_CODE_MULTI_PLAYER_GAME_RESULT = 3;

    // ABOUT APP
    String URL_FACEBOOK_PROGRAMER_PROFILE = "https://www.facebook.com/andrebento16";
    String URL_GITHUB = "https://github.com/andrepbento/InformaticsQuiz";
    String URL_ISEC = "https://www.isec.pt/PT/Default.aspx";
}
