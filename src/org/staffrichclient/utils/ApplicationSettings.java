package org.staffrichclient.utils;

import java.util.prefs.Preferences;
import java.awt.Dimension;
 
public class ApplicationSettings 
{
    private Preferences userPrefs;
 
    public ApplicationSettings()
    {
        userPrefs = Preferences.userNodeForPackage(ApplicationSettings.class);
    }
    
    public String getUsername()
    {
        String par = userPrefs.get("username", "");
        return par;
    }
    
    public String getPassword()
    {
        String par = userPrefs.get("password", "");
        return par;
    }
    
    public void savePassword(String password)
    {	
        userPrefs.put("password", password);
    }
        
    public void saveUsername(String username)
    {
        userPrefs.put("username", username);
    }
 
}