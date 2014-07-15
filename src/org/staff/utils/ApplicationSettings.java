package org.staff.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;
import java.awt.Dimension;

import org.eclipse.swt.widgets.DateTime;
 
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
 
    public boolean getHideDone()
    {
        boolean par = userPrefs.getBoolean("hidedone", false);
        return par;
    }
    
    public void saveHideDone(boolean hidedone)
    {	
        userPrefs.putBoolean("hidedone", hidedone);
    }
    
    public String getTaskSearchPattern()
    {
    	String par = userPrefs.get("tasksearchpattern", "");
        return par;
    }
    
    public void saveHideDone(String tasksearchpattern)
    {	
        userPrefs.put("tasksearchpattern", tasksearchpattern);
    }
    
    public DateTime getDateBegin()
    {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd.yyyy");
       	
    	String par = userPrefs.get("datebegin", "");
    	
    	Date date = null;
       
    	try
        {
            date = simpleDateFormat.parse(par);

            System.out.println("date : "+simpleDateFormat.format(date));
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }
    	
    	//DateTime dt = date.;
    	
    	return null;
    }
    
    public void saveDateBegin(DateTime dateBegin)
    {	
        //userPrefs.put("tasksearchpattern", tasksearchpattern);
    }
    
}