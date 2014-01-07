package eu.sqooss.impl.service.webadmin;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.service.logging.Logger;

public enum Translation implements ITranslation {
	EN(Locale.ENGLISH),
	DE(Locale.GERMAN);
	// Add more locales through Locale.CONSTANT or new Locale("ISO 639 country code");

	// Names of the various resource files
	private static final String RES_DIR			  = "translation/webadmin/";
	private static final String RES_LABELS_FILE   = RES_DIR+"ResourceLabels";
	private static final String RES_ERRORS_FILE   = RES_DIR+"ResourceErrors";
	private static final String RES_MESSAGES_FILE = RES_DIR+"ResourceMessages";

	// Resource bundles
	private ResourceBundle resLbl;
	private ResourceBundle resMsg;
	private ResourceBundle resErr;

	private Locale locale;

	Translation(Locale locale) {
		this.locale = locale;
		try {
			resLbl = ResourceBundle.getBundle(RES_LABELS_FILE,locale);
			resMsg = ResourceBundle.getBundle(RES_MESSAGES_FILE,locale);
			resErr = ResourceBundle.getBundle(RES_ERRORS_FILE,locale);
		}
		catch(MissingResourceException e) {
			try{
				AlitheiaCore.getInstance().getLogManager().createLogger(Logger.NAME_SQOOSS_TRANSLATION)
				.error("Missing resource file for locale " + locale.getLanguage(), e);
			}
			catch(Exception e2){
				// We can't even get a logger...
				// Silently fail, the error will become apparent because of missing translations
			}
		}
	}

	@Override
	public String label(String id) {
		return getTranslation(resLbl, id);
	}

	@Override
	public String message(String id) {
		return getTranslation(resMsg, id);
	}

	@Override
	public String error(String id) {
		return getTranslation(resErr, id);
	}

	private String getTranslation(ResourceBundle rb, String id) {
		try {
			return rb.getString(id);
		}
		catch (NullPointerException | MissingResourceException ex) {
			return id;
		}
	}

	@Override
	public Locale getLocale() {
		return locale;
	}
}
