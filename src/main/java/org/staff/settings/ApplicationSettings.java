package org.staff.settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.DateTime;
import org.staff.utils.Convertor;
 
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
    
    public void saveTaskSearchPattern(String tasksearchpattern)
    {	
        userPrefs.put("tasksearchpattern", tasksearchpattern);
    }
    
    public GregorianCalendar getDateBegin()
    {
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
    	String par = userPrefs.get("datebegin", sdf.format(date));
    	return Convertor.stringToGregorianCalendar(par);
    }
    
    public void saveDateBegin(GregorianCalendar gregorianCalendar)
    {	
       userPrefs.put("datebegin", Convertor.gregorianCalendarToString(gregorianCalendar));
    }

	public GregorianCalendar getDateEnd() {
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		String par = userPrefs.get("dateend", sdf.format(date));
    	return Convertor.stringToGregorianCalendar(par);
    }
	
	public void saveDateEnd(GregorianCalendar gregorianCalendar) {
	    userPrefs.put("dateend", Convertor.gregorianCalendarToString(gregorianCalendar));
	}
    
}