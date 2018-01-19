package me.quin.buildtoguess.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProfileSaver extends BukkitRunnable {

    private Profile profile;

    private static final String SAVE = "UPDATE ze_players SET coins=?";
    
    public ProfileSaver()
    {
    }

    @Override
    public void run() 
    {
        Connection connection = null;

        try 
        {
            connection = profile.getBuildToGuess().getDatabas().getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SAVE);
            preparedStatement.setInt(1, profile.getCoins());
            preparedStatement.setString(2, profile.getUuid().toString());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) 
        {
            e.printStackTrace();
        } finally {
            if (connection != null) 
            {
                try {
                    connection.close();
                } catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }

}