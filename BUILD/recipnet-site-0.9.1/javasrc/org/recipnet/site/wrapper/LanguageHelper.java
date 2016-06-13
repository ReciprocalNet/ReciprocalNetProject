/*
 * Reciprocal Net project
 * 
 * LanguageHelper.java
 *
 * 07-Jun-2004: midurbin wrote the first draft
 * 14-Jun-2004: midurbin consolidated the three resource bundle groups into one
 * 04-Oct-2004: midurbin added getDefaultLocales()
 * 20-0ct-2004: midurbin added getHoldingLevelString()
 * 14-Mar-2005: midurbin added getNumberComparisonOperatorString()
 * 22-Jun-2005: midurbin added getDaysAgoString() and getSinceDaysAgoString()
 * 13-Jul-2005: midurbin added getAuthorizationReasonString()
 * 05-Aug-2005: midurbin added getFieldUnits()
 * 02-Nov-2005: midurbin fixed bug #1673 in getBestBundle()
 * 11-Apr-2006: jobollin switched to initializing object caches via
 *              ObjectCache.newInstance(); removed unused imports
 */

package org.recipnet.site.wrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.recipnet.common.ObjectCache;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.logevent.LanguageHelperLogEvent;

/**
 * <p>
 * {@code LangaugeHelper} is a {@code ContextListener} that manages
 * {@code ResourceBundle}s that contain localized strings that are associated
 * with constant code values in container objects. Specifically sample states,
 * actions, fields as well as repository holding levels are included in the
 * resource bundles.
 * </p><p>
 * Like other listeners, to use this class, simply invoke the {@code extract()}
 * method to get a reference and then use the various public methods to get
 * localized {@code String} representations of container code values.
 * </p>
 */
public class LanguageHelper implements ServletContextListener {

    /**
     * A constant {@code ResourceBundle} object, that when referenced in a
     * cache, indicates that there is currently no {@code ResourceBundle} for
     * the locale (used to generate the key for the cache) available.
     */
    private static final ResourceBundle UNSUPPORTED_LOCALE
            = LanguageHelper.AccessiblePropertyResourceBundle.getDummyBundle();

    /**
     * <p>
     * Cache of {@code ResourceBundle} objects. The primary key needed by
     * {@code ObjectCache} is ignored in this case (a unique, incrimental value
     * is used) and the caches are indexed by {@code String} objects generated
     * by {@code Locale.getString()} for the locale to which the
     * {@code ResourceBundle} pertains concatanated with the 'suffix' to
     * indicate whether the marked up HTML version is requested or not.
     * </p><p>
     * For example, a {@code ResourceBundle} for the US English {@code Locale}
     * marked up in HTML, would be stored using the key 'en_US-html.strings'.
     * The non-marked-up version would have the key 'en_US.strings'. (the suffix
     * is "-html.strings" or just ".strings" because they are also used in
     * filename generation) For an unspecified (null) locale, they key would be
     * 'null.strings' or 'null-html.strings'.
     * </p><p>
     * If the value is a reference to {@code UNSUPPORTED_LOCALE}, this
     * indicates that an unsuccessful attempt to locate a {@code ResourceBundle}
     * for the given locale has already been performed.
     * </p>
     */
    private ObjectCache<ResourceBundle> siteStringCache;

    /**
     * An JAR file containing localization property files, loaded from the path
     * supplied by an optional context parameter in web.xml. If present, it is
     * checked first when attempting to load a new {@code ResourceBundle}.
     */
    private JarFile languagePackJar;

    /**
     * An integer, initialized during {@code contextInitialized()} that is
     * incremented during {@code getBundle()} when its value is used as a dummy
     * primary key so that it will always be a unique key.
     */
    private int nextCachePrimaryKey;

    /**
     * Static function that returns an instance of {@code LanguageHelper},
     * given the {@code ServletContext} object from the current web application.
     * 
     * @param sc the {@code ServletContext} object for the current web
     *        application, as obtained by a call to
     *        {@code ServletConfig.getServletContext()} .
     * @return the {@code LanguageHelper} configured on the specified context
     * @throws IllegalArgumentException if no {@code LanguageHelper} instance
     *         has been embedded within the specified {@code ServletContext}.
     */
    public static LanguageHelper extract(ServletContext sc) {
        LanguageHelper languageHelper = (LanguageHelper) sc.getAttribute(
                LanguageHelper.class.getName());
        
        if (languageHelper == null) {
            throw new IllegalArgumentException();
        } else {
            return languageHelper;
        }
    }

    /**
     * Returns an {@code Enumeration} containing the default {@code Locale} for
     * this system. This method is useful to generate the 'locales' argument for
     * various methods on this page when they are being used to generate values
     * that are localized for the currently running java virtual machine. This
     * method should not be used to localize values that will be displayed on
     * HTML pages returned to web users. Instead,
     * {@code ServletRequest.getLocales()} should be used.
     * 
     * @return an {@code Enumeration} containing the default locale for the java
     *         system on which this is running
     */
    public static Enumeration<? extends Locale> getDefaultLocales() {
        return new SystemDefaultLocaleEnumeration();
    }

    /**
     * Returns a {@code String} representation of the given status code in the
     * language that best matches those indicated by 'locales'.
     * 
     * @param statusCode a status code, as defined in {@code SampleInfo}.
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the result may be marked
     *        up in HTML.
     * @return the {@code String} representation of the given status code
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getStatusString(int statusCode,
            Enumeration<? extends Locale> locales,
            boolean htmlMarkup) throws IOException, ResourceNotFoundException {
        return getSiteStringBundle(locales, htmlMarkup).getString(
                "status" + String.valueOf(statusCode));
    }

    /**
     * Returns a {@code String} representation of the given action code in the
     * language that best matches those indicated by 'locales'.
     * 
     * @param actionCode a action code, as defined in {@code SampleHistoryInfo}.
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the result may be marked
     *        up in HTML.
     * @return the {@code String} representation of the given action code
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getActionString(int actionCode,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        return getSiteStringBundle(locales, htmlMarkup).getString(
                "action" + String.valueOf(actionCode));
    }

    /**
     * Returns a {@code String} representation of the given field code in the
     * language that best matches those indicated by 'locales'.
     * 
     * @param fieldCode a field code, as defined in {@code SampleInfo},
     *        {@code SampleDataInfo} or {@code SampleTextInfo}.
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the results may be marked
     *        up in HTML.
     * @return the {@code String} representation of the given field code
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getFieldString(int fieldCode,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        return getSiteStringBundle(locales, htmlMarkup).getString(
                "field" + String.valueOf(fieldCode));
    }

    /**
     * Returns a {@code String} representation of the given holding level in the
     * language that best matches those indicated by 'locales'.
     * 
     * @param holdingLevel one of the constant holding levels defined in
     *        {@code RepositoryHoldingInfo}.
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the results may be marked
     *        up in HTML.
     * @return the {@code String} representation of the given holding level
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getHoldingLevelString(int holdingLevel,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        return getSiteStringBundle(locales, htmlMarkup).getString(
                "holdingLevel" + String.valueOf(holdingLevel));
    }

    /**
     * Returns a {@code String} representation of the given number comparison
     * operator in the language that best matches those indicated by 'locales'.
     * 
     * @param operatorCode one of the constant operator codes defined in
     *        {@code NumberComparisonSC}.
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the results may be marked
     *        up in HTML.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getNumberComparisonOperatorString(int operatorCode,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        return getSiteStringBundle(locales, htmlMarkup).getString(
                "numberComparison" + String.valueOf(operatorCode));
    }

    /**
     * Returns a {@code String} representation of the day that is the given
     * number of days ago in the language that best matches those indicated by
     * 'locales'. Typical results might include: "yesterday", "3 days ago" or
     * "about 2 weeks ago"
     * <p>
     * The current implementation seeks a specific entry in the resource bundle
     * for the number of days ago listed (ie, "daysAgo2"). If such a perfect
     * match is found, the resulting value is returned. Otherwise if the number
     * of days ago is less than a week, the generic 'daysAgo*' value is used by
     * substituting the supplied days ago for the wildcard character in the
     * value. If the number of days ago is greater than a week, an identical
     * proceedure is followed, but for the specific and then generic "weeksAgo"
     * keys rather than the "daysAgo" keys.
     * 
     * @param daysAgo the number of days ago; zero represents today
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the results may be marked
     *        up in HTML.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getDaysAgoString(int daysAgo,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        ResourceBundle strings = getSiteStringBundle(locales, htmlMarkup);
        String localizedString = null;
        
        if (daysAgo < 7) {
            try {
                localizedString = strings.getString("daysAgo"
                        + String.valueOf(daysAgo));
            } catch (MissingResourceException ex) {
                // no exact value; try wildcard
                localizedString = strings.getString("daysAgo*").replaceAll(
                        "\\*", String.valueOf(daysAgo));
            }
        } else {
            int weeks = 0;
            
            try {
                weeks = (daysAgo + 3) / 7;
                localizedString = strings.getString("weeksAgo"
                        + String.valueOf(weeks));
            } catch (MissingResourceException ex) {
                // no exact value; try wildcard
                localizedString = strings.getString("weeksAgo*").replaceAll(
                        "\\*", String.valueOf(weeks));
            }
        }
        return localizedString;
    }

    /**
     * <p>
     * Returns a {@code String} representation of the elapsed days in the
     * language that best matches those indicated by 'locales'. Typical results
     * might include: "since yesterday" or "during the last 3 days".
     * </p><p>
     * The current string-picking algoritm is identical to that for the 'days
     * ago' strings except that the keys are only indexed by days
     * ("sinceDaysAgo0", "sinceDaysAgo1", "sinceDaysAgo*") and not weeks.
     * </p>
     * 
     * @param days the number of days elapsed; zero represents today
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the results may be marked
     *        up in HTML.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getSinceDaysAgoString(int days,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        ResourceBundle strings = getSiteStringBundle(locales, htmlMarkup);
        
        try {
            return strings.getString("sinceDaysAgo" + String.valueOf(days));
        } catch (MissingResourceException ex) {
            // no exact value; fall through for wildcard
        }
        
        return strings.getString("sinceDaysAgo*").replaceAll("\\*",
                String.valueOf(days));
    }

    /**
     * Returns a {@code String} representation of the error message for the
     * given 'reasonCode'. These error codes are defined in {@link
     * org.recipnet.site.content.rncontrols.AuthorizationReasonMessage
     * AuthorizationReasonMessage}.
     * 
     * @param reasonCode the reasonCode for an authorization failure message
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the results may be marked
     *        up in HTML.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getAuthorizationReasonString(int reasonCode,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        return getSiteStringBundle(locales, htmlMarkup).getString(
                "AuthError" + String.valueOf(reasonCode));
    }

    /**
     * Returns a {@code String} representation of the units for the given
     * 'fieldCode'. If no units are found for the 'fieldCode' an empty
     * {@code String} is returned.
     * 
     * @param fieldCode a code indicating a metadata field for a Sample
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the results may be marked
     *        up in HTML.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public String getFieldUnits(int fieldCode,
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        try {
            return getSiteStringBundle(locales, htmlMarkup).getString(
                    "units" + String.valueOf(fieldCode));
        } catch (MissingResourceException ex) {
            return "";
        }
    }

    /**
     * Returns the {@code ResourceBundle} whose {@code Locale} most closely
     * matches those indicated in the prioritized {@code Enumeration},
     * 'locales'. The keys for the returned bundle are {@code Strings} generated
     * by concatenating 'action', 'field' or 'status' (depending on the type of
     * code) with the {@code String} representations of the numeric codes
     * defined on the container object. The values are localized human-readable
     * {@code String} representations of the given code.
     * <p>
     * Example: The value for field code 50 is stored under the key "field50".
     * The value for action code 100 is stored under the key "action100". The
     * value for status 200 is stored under the key "status200".
     * 
     * @param locales an {@code Enumeration} of {@code Locale}s, from most
     *        desirable to least desirable.
     * @param htmlMarkup a boolean indicating whether the result may be marked
     *        up in HTML.
     * @return a {@code ResourceBundle} assigning {@code String}s to each code
     *         number.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    public ResourceBundle getSiteStringBundle(
            Enumeration<? extends Locale> locales, boolean htmlMarkup)
            throws IOException, ResourceNotFoundException {
        return getBestBundle("site", locales, (htmlMarkup ? "-html" : "")
                + ".strings", this.siteStringCache);
    }

    /**
     * Returns the {@code ResourceBundle} that best matches the prioritized
     * {@code Enumeration} of {@code Locale}s using the following algorithm.
     * <ol>
     * <li>if any {@code ResourceBundle}s exist that match any of the
     * {@code Locale}s' language and country, the one matching the highest
     * priority (first in the enumeration) locale will be returned. (The
     * 'variant' of any provided locale will be ignored.)</li>
     * <li>if no complete matches exist, the parent is taken from each locale
     * in order and the highest priority matching bundle will be returned.
     * <br />
     * Note: the parent of a locale is defined as a locale that matches all but
     * the most specific (right-most) characteristic. For example: 'en' is the
     * parent of 'en_US' In the case where only the language (ie, 'en') is
     * listed, the parent is not meaningful because it contains no information
     * about the locale. Such a parent is indicated by null.</li>
     * <li>if no matches are found then the default localization bundle is
     * returned.</li>
     * </ol>
     * 
     * @param prefix the first part of the name of the resource property file.
     *        This corresponds with the cache provided . See
     *        {@code getPropertyFileName()} for details.
     * @param locales an {@code Enumeration} of {@code Locale} objects, ordered
     *        from highest priority to lowest priority.
     * @param suffix the last part of the name of the resource property file,
     *        including any file extension (also used in the cache key). See
     *        {@code getPropertyFileName()} for details.
     * @param cache an {@code ObjectCache} for use by {@code getBundle()} in
     *        caching {@code ResourceBundle}s.
     * @return the most appropritate {@code ResourceBundle} for the given
     *         {@code Locale} objects.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     * @throws ResourceNotFoundException if no fall-back resource bundle is
     *         available on the system.
     */
    private ResourceBundle getBestBundle(String prefix,
            Enumeration<? extends Locale> locales, String suffix,
            ObjectCache<ResourceBundle> cache) throws IOException,
            ResourceNotFoundException {
        ResourceBundle bundle = LanguageHelper.UNSUPPORTED_LOCALE;
        Collection<Locale> parentLocaleList = new ArrayList<Locale>();

        /*
         * looking for an exact match amongst the requested locales, while
         * building a contingency list of the parents of such locales in case no
         * exact match is found
         */
        while (locales.hasMoreElements()) {
            // get bundle for next locale
            Locale locale = locales.nextElement();
            
            if (!locale.getVariant().equals("")) {
                locale = new Locale(locale.getLanguage(), locale.getCountry());
            }
            bundle = getBundle(prefix, locale, suffix, cache);
            if (bundle != LanguageHelper.UNSUPPORTED_LOCALE) {
                /*
                 * a bundle was found, and since we've gone in order from
                 * highest priority to lowest priority locales, it is the best
                 * possible match
                 */
                return bundle;
            } else {
                /*
                 * because no bundle existed for this exact locale, add this
                 * locale's parent locale to the end of the list of locales for
                 * consideration in turn
                 */
                Locale parentLocale = LanguageHelper.getParentLocale(locale);
                
                if ((parentLocale != null)
                        && !parentLocaleList.contains(parentLocale)) {
                    parentLocaleList.add(parentLocale);
                }
            }
        }

        /*
         * No match has been found in the enumeration, but now we have a list of
         * the parent locales for the original enumeration, which maintians its
         * prioritized ordering.
         */
        for (Locale locale : parentLocaleList) {
            bundle = getBundle(prefix, locale, suffix, cache);
            if (bundle != LanguageHelper.UNSUPPORTED_LOCALE) {
                /*
                 * a bundle was found, and since we've gone in order from
                 * highest priority to lowest priority locales, it is the best
                 * possible match
                 */
                return bundle;
            }
        }

        // no appropriate bundle has been found, return the fallback bundle
        bundle = getBundle(prefix, null, suffix, cache);
        if (bundle == LanguageHelper.UNSUPPORTED_LOCALE) {
            throw new ResourceNotFoundException();
        }
        
        return bundle;
    }

    /**
     * Gets a {@code ResourceBundle} from either the cache, or other sources if
     * one exists that matches the given {@code Locale}. This method will
     * update the cache to reflect its experience searching for a new
     * {@code ResourceBundle} either by caching the bundle or indicating that it
     * is a known deficiency.
     * 
     * @param prefix the first part of the name of the resource property file.
     *        This corresponds with the cache provided . See
     *        {@code getPropertyFileName()} for details.
     * @param locale a {@code Locale} to which the returned
     *        {@code ResourceBundle} must match.
     * @param suffix the last part of the name of the resource property file,
     *        including any file extension (also used in the cache key). See
     *        {@code getPropertyFileName()} for details.
     * @param cache an {@code ObjectCache} for use by {@code getBundle()} in
     *        caching {@code ResourceBundle}s.
     * @return a {@code ResourceBundle} whose locale matches the provided
     *         locale, or {@code LanguageHelper.UNSUPPORTED_LOCALE} if no match
     *         was found.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     */
    private ResourceBundle getBundle(String prefix, Locale locale,
            String suffix, ObjectCache<ResourceBundle> cache)
            throws IOException {
        String bundleKeyName = locale + suffix;
        ResourceBundle bundle = cache.get(bundleKeyName);

        if (bundle == null) {
            bundle = loadBundle(prefix, locale, suffix, cache);
            cache.put(this.nextCachePrimaryKey++, bundleKeyName, bundle);
        }

        return bundle;
    }

    /**
     * Attempts to load a {@code ResourceBundle} from the
     * {@code languagePackJar}, if available, and then from this class'
     * {@code ClassLoader} object. This method may call {@code getBundle()} in
     * order to efficiently determine the parent {@code ResourceBundle} which
     * may in turn call this function recursively.
     * 
     * @param prefix the first part of the name of the resource property file.
     *        This corresponds with the cache provided . See
     *        {@code getPropertyFileName()} for details.
     * @param locale a {@code Locale} to which the returned
     *        {@code ResourceBundle} must match.
     * @param suffix the last part of the name of the resource property file,
     *        including any file extension. See {@code getPropertyFileName()}
     *        for details.
     * @param cache an {@code ObjectCache} for use by {@code getBundle()} in
     *        caching {@code ResourceBundle}s.
     * @return a {@code ResourceBundle} whose locale matches the provided
     *         locale, or {@code LanguageHelper.UNSUPPORTED_LOCALE} if no match
     *         was found.
     * @throws IOException if an error is encountered while loading a properties
     *         file.
     */
    private ResourceBundle loadBundle(String prefix, Locale locale,
            String suffix, ObjectCache<ResourceBundle> cache)
            throws IOException {
        ResourceBundle bundle = null;
        
        if (this.languagePackJar != null) {
            // attempt to load the resource from the language pack jar file
            ZipEntry ze = this.languagePackJar.getEntry(
                    LanguageHelper.getPropertyFileName(prefix, locale, suffix));
            if (ze != null) {
                InputStream is = this.languagePackJar.getInputStream(ze);
                bundle = new LanguageHelper.AccessiblePropertyResourceBundle(
                        is, locale);
            } else {
                // no bundle found, fall through
            }
        }
        if (bundle == null) {
            // attempt to load the resource using the ClassLoader
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream(
                    LanguageHelper.getPropertyFileName(prefix, locale, suffix));
            
            if (is != null) {
                bundle = new LanguageHelper.AccessiblePropertyResourceBundle(
                        is, locale);
            } else {
                return LanguageHelper.UNSUPPORTED_LOCALE;
            }
        }

        // if bundle == null, we would have returned by now
        assert (bundle != null);

        if (locale != null) {
            // this call may be recursive
            ((LanguageHelper.AccessiblePropertyResourceBundle) bundle).setParent(
                    getBundle(prefix, LanguageHelper.getParentLocale(locale),
                            suffix, cache));
        }
        
        return bundle;
    }

    /**
     * Determines the name of the property file in which data for the given
     * {@code Locale} with the given 'prefix' and 'suffix' would reside. The
     * result is simply a concatination of the prefix, {@code String}
     * representation of the 'locale' followed by the 'suffix'. One hyphen is
     * inserted before the 'locale' if it is not null.
     * 
     * @param prefix the first part of the filename; usually identifies the type
     *        of data strings that are contained in the {@code ResourceBundle}.
     * @param locale the {@code Locale} of the property file
     * @param suffix the last part of the filename; this includes the file
     *        extension and any other modifying text. This can be used to
     *        introduce factors that are not part of {@code Locale}, such as
     *        having HTML versions of {@code ResourceBundle}s.
     * @return the name of the property file for the given prefix, locale and
     *         suffix.
     */
    private static String getPropertyFileName(String prefix, Locale locale,
            String suffix) {
        return (prefix + ((locale == null) ? "" : "-" + locale.toString())
                + ((suffix == null) ? "" : suffix));
    }

    /**
     * Determines and returns the next most general {@code Locale}. In the case
     * where a language, country and variation is specified, this method will
     * return a {@code Locale} with the same language and country specified, but
     * with no specified variation. When language and country is specified, the
     * parent will simply be a {@code Locale} describing just the desired
     * language. In the case where only a language is specified, null is
     * returned. In the event that the specified {@code Locale} is null, null is
     * returned.
     */
    private static Locale getParentLocale(Locale locale) {
        if ((locale == null) || locale.getCountry().equals("")) {
            /*
             * null indicates that no useful information is available (no
             * language, country or variant)
             */
            return null;
        } else if (locale.getVariant().equals("")) {
            /*
             * language and country are defined, but not variant; the parent
             * will only have the language defined
             */
            return new Locale(locale.getLanguage());
        } else {
            /*
             * country, language and variant are defined, the parent will only
             * have the language and country defined.
             */
            return new Locale(locale.getLanguage(), locale.getCountry());
        }
    }

    /**
     * From interface {@code ServletContextListener}. Reads and parses
     * configuration directives specified in web.xml. Causes this object to be
     * persisted in the webapp's Application context in such a way that
     * {@code extract()} will work.
     */
    public void contextInitialized(ServletContextEvent sce) {

        // initialize the 'automatic' unique key source
        this.nextCachePrimaryKey = 0;

        // Read and parse configuration directives.
        String cacheConfiguration = sce.getServletContext().getInitParameter(
                "languageCacheParameterString");

        String path = sce.getServletContext().getInitParameter(
                "languagePackJar");
        if ((path == null) || path.equals("")) {
            this.languagePackJar = null;
        } else {
            try {
                this.languagePackJar = new JarFile(new File(path));
            } catch (IOException ex) {
                CoreConnector coreConnector
                        = CoreConnector.extract(sce.getServletContext());
                try {
                    coreConnector.getSiteManager().recordLogEvent(
                            new LanguageHelperLogEvent(path, ex));
                } catch (RemoteException re) {
                    coreConnector.reportRemoteException(re);
                }
            }
        }

        this.siteStringCache = ObjectCache.newInstance(cacheConfiguration);

        // Persist this object in the Application context.
        sce.getServletContext().setAttribute(LanguageHelper.class.getName(),
                this);
    }

    /**
     * From interface {@code ServletContextListener}. Removes this object from
     * the webapp's Application context.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        // Remove this object from the Application context.
        sce.getServletContext().removeAttribute(LanguageHelper.class.getName());
    }

    /**
     * Extends {@code PropertyResourceBundle} simply to allow access to
     * protected members.
     */
    private static class AccessiblePropertyResourceBundle extends
            PropertyResourceBundle {

        /**
         * The superclass most probably determines locale by variations in the
         * class name, but for ease and performance, this class will store the
         * locale and override {@code getLocale()}.
         */
        private Locale locale;

        /**
         * Simply sets {@code locale} and delegates to the superclass.
         */
        public AccessiblePropertyResourceBundle(InputStream is, Locale locale)
                throws IOException {
            super(is);
            this.locale = locale;
        }

        /**
         * Creates and returns a new {@code ResourceBundle} that contains no
         * resources.
         * 
         * @return an empty {@code ResourceBundle}
         */
        public static ResourceBundle getDummyBundle() {
            return new ListResourceBundle() {
                private final Object[][] contents = new Object[2][0];
                
                @Override
                protected Object[][] getContents() {
                    return contents;
                }
            };
        }

        /**
         * Overrides {@code PropertyResourceBundle}; the current implementation
         * simply returns {@code locale}
         */
        @Override
        public Locale getLocale() {
            return this.locale;
        }

        /**
         * Overrides {@code PropertyResourceBundle} for the sole purpose of
         * making the method public.
         */
        @Override
        public void setParent(ResourceBundle parent) {
            super.setParent(parent);
        }
    }

    /**
     * A class for the purpose of having an {@code Enumeration} that contains
     * the system's default {@code Locale}.
     */
    private static class SystemDefaultLocaleEnumeration
            implements Enumeration<Locale> {

        /**
         * Keeps track of whether the single enumerated value has been returned.
         */
        private boolean usedUp = false;

        /** implements {@code Enumeration} */
        public boolean hasMoreElements() {
            return !this.usedUp;
        }

        /**
         * Implements {@code Enumeration} to return the system default locale
         * the first time this method is called.
         * 
         * @return a {@code Locale} representing the system default locale
         * @throws NoSuchElementException if called when
         *         {@code hasMoreElements()} returns false
         */
        public Locale nextElement() {
            if (this.usedUp) {
                throw new NoSuchElementException();
            }
            this.usedUp = true;
            return Locale.getDefault();
        }
    }
}
