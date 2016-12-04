package models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by andre on 03/12/2016.
 */

public class MySharedPreferences {
    String sharedTitle = "informaticsQuizPreferences";
    SharedPreferences sharedPreferences;

    public int gameInfoLenght;

    public MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(sharedTitle, Context.MODE_PRIVATE);
//        gameInfoLenght = shared.getInt("gameInfoLenght",0);
		/*SharedPreferences.Editor editor = shared.edit();
		editor.clear();
		editor.commit();*/
//        fillPlayerGameInfo();

    }
/*
    public void saveConfigData(int lines, int columns, int touchMode,boolean intruderPairs, String theme) {
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("lines", lines);
        editor.putInt("columns", columns);
        editor.putString("theme", theme);
        editor.putInt("touchMode", touchMode);
        editor.putBoolean("intruderPairs", intruderPairs);
        editor.apply();
    }

    public int getConfigLines() 	{
        return shared.getInt("lines",4);
    }
    public int getConfigCols()	 	{
        return shared.getInt("columns",3);
    }
    public int getConfigTouchMode() {
        return shared.getInt("touchMode",1);
    }
    public String getConfigTheme(){
        return shared.getString("theme", "food");
    }
    public Boolean getIntruderPair(){
        return shared.getBoolean("intruderPairs", false);
    }



    public void savePlayerGameInfo( GameInfo gi)
    {
        gameInfo.add(gi);

        int id = gameInfo.size()-1;
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("" + id , gi.toCodedString());
        editor.putInt("gameInfoLenght", gameInfo.size());
        editor.apply();
    }


    public void fillPlayerGameInfo()
    {
        for (Integer i = 0 ; i<gameInfoLenght ; i++)
        {
            String playerInfo = shared.getString(  i.toString()  , "Nothing Set");
            gameInfo.add( new GameInfo(playerInfo) );
        }

    }
    */
}
