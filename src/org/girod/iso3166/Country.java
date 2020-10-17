/*
MIT License

Copyright (c) 2020 Hervé Girod

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

Alternatively if you have any questions about this project, you can visit
the project website at the project page on https://github.com/hervegirod/countryiso3166
 */
package org.girod.iso3166;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

/**
 * Represent one ISO 3166-1 Country.
 *
 * @since 0.1
 */
public class Country {
   private static Cache CACHE = null;
   private final String name;
   private final String alpha2;
   private final String alpha3;
   private List<String> altNames = null;
   private final int numeric;

   /**
    * Constructor.
    *
    * @param name the country name
    * @param alpha2 the country alpha-2 code
    * @param alpha3 the country alpha-3 code
    * @param numeric numeric code as an int
    */
   public Country(String name, String alpha2, String alpha3, int numeric) {
      this.name = name;
      this.alpha2 = alpha2;
      this.alpha3 = alpha3;
      this.numeric = numeric;
      createCache();
      addCountryToCache();
   }

   /**
    * Constructor.
    *
    * @param name the country name
    * @param altNames the alternate names
    * @param alpha2 the country alpha-2 code
    * @param alpha3 the country alpha-3 code
    * @param numeric numeric code as an int
    */
   public Country(String name, List<String> altNames, String alpha2, String alpha3, int numeric) {
      this(name, altNames, alpha2, alpha3, numeric, true);
   }

   /**
    * Constructor.
    *
    * @param name the country name
    * @param altNames the alternate names
    * @param alpha2 the country alpha-2 code
    * @param alpha3 the country alpha-3 code
    * @param numeric numeric code as an int
    * @param true if the Country lust be added to the cache
    */
   Country(String name, List<String> altNames, String alpha2, String alpha3, int numeric, boolean addToCache) {
      this.name = name;
      this.alpha2 = alpha2;
      this.alpha3 = alpha3;
      this.numeric = numeric;
      this.altNames = altNames;
      if (addToCache) {
         createCache();
         if (altNames == null) {
            addCountryToCache();
         } else {
            addCountryToCache(altNames);
         }
      }
   }

   public static void main(String[] args) {
      URL url = Country.class.getResource("country.properties");
      try {
         PropertyResourceBundle prb = new PropertyResourceBundle(url.openStream());
         String version = prb.getString("version");
         String date = prb.getString("date");
         System.out.println("CountryISO166 version " + version + " build on " + date);
         System.out.println("Distributed under the MIT license");
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   /**
    * Return the list of countries.
    *
    * @return the list of countries
    */
   public List<Country> listCountries() {
      List<Country> list = new ArrayList<>();
      Iterator<Country> it = CACHE.countryByName.values().iterator();
      while (it.hasNext()) {
         list.add(it.next());
      }
      return list;
   }

   /**
    * Return the country of a specified name. Return null if there is no country for this name.
    *
    * @param name the country name
    * @return the country
    */
   public static Country getCountryFromName(String name) {
      createCache();
      return CACHE.countryByName.get(name);
   }

   /**
    * Return the country of a specified alpha-2 code. Return null if there is no country for this code.
    *
    * @param alpha2 the country alpha2 code
    * @return the country
    */
   public static Country getCountryFromAlpha2(String alpha2) {
      createCache();
      return CACHE.countryByAlpha2.get(alpha2);
   }

   /**
    * Return the country of a specified alpha-3 code. Return null if there is no country for this code.
    *
    * @param alpha3 the country alpha3 code
    * @return the country
    */
   public static Country getCountryFromAlpha3(String alpha3) {
      createCache();
      return CACHE.countryByAlpha3.get(alpha3);
   }

   /**
    * Return the country of a specified numeric code. Return null if there is no country for this code.
    *
    * @param numeric the country numeric code
    * @return the country
    */
   public static Country getCountryFromNumeric(int numeric) {
      createCache();
      return CACHE.countryByNum.get(numeric);
   }

   /**
    * Return the country of a specified numeric code. Return null if there is no country for this code.
    *
    * @param numeric the country numeric code as a String
    * @return the country
    */
   public static Country getCountryFromNumeric(String numeric) {
      createCache();
      if (numeric.length() != 3) {
         return null;
      }
      try {
         int num = Integer.parseInt(numeric);
         return getCountryFromNumeric(num);
      } catch (NumberFormatException e) {
         return null;
      }
   }

   /**
    * Return the country name.
    *
    * @return the country name
    */
   public String getName() {
      return name;
   }

   /**
    * Return the country alternate names. Note that this notion does not exist in the ISO 3166-1 standard. But it helps to
    * for "United Kingdom of Great Britain and Northern Ireland".
    *
    * @return the country alternate names
    */
   public List<String> getAlternateNames() {
      return altNames;
   }

   /**
    * Return true if the country has alternate names.
    *
    * @return true if the country has alternate names
    */
   public boolean hasAlternateNames() {
      return altNames != null;
   }

   /**
    * Return the country alpha-2 code.
    *
    * @return the country alpha-2 code
    */
   public String getAlpha2() {
      return alpha2;
   }

   /**
    * Return the country alpha-3 code.
    *
    * @return the country alpha-3 code
    */
   public String getAlpha3() {
      return alpha3;
   }

   /**
    * Return the country numeric code as an int.
    *
    * @return the country numeric code
    */
   public int getNumericCode() {
      return numeric;
   }

   /**
    * Return the country numeric code as a 3 digit String.
    *
    * @return the country numeric code
    */
   public String getFormattedNumericCode() {
      String s = Integer.toString(numeric);
      if (s.length() < 3) {
         s = "00".substring(0, 3 - s.length()) + s;
      }
      return s;
   }

   /**
    * Return the country ISO 3166-2 code.
    *
    * @return the country ISO 3166-2 code
    */
   public String getISO31662() {
      return "ISO 3166-2:" + alpha2;
   }

   private static void createCache() {
      if (CACHE == null) {
         CACHE = new Cache();
      }
   }

   private void addCountryToCache() {
      CACHE.addCountryToCache(this);
   }

   private void addCountryToCache(List<String> altNames) {
      CACHE.addCountryToCache(this, altNames);
   }

   static class Cache {
      final Map<String, Country> countryByName = new HashMap<>();
      final Map<String, Country> countryByAlpha2 = new HashMap<>();
      final Map<String, Country> countryByAlpha3 = new HashMap<>();
      final Map<Integer, Country> countryByNum = new HashMap<>();

      private Cache() {
         parseSpec();
      }

      private void parseSpec() {
         CountryParser parser = new CountryParser(this);
         parser.parseSpec();
      }

      private void addCountryToCache(Country country) {
         countryByName.put(country.name, country);
         countryByAlpha2.put(country.alpha2, country);
         countryByAlpha3.put(country.alpha3, country);
         countryByNum.put(country.numeric, country);
      }

      private void addCountryToCache(Country country, List<String> altNames) {
         addCountryToCache(country);
         Iterator<String> it = altNames.iterator();
         while (it.hasNext()) {
            String altName = it.next();
            countryByName.put(altName, country);
         }
      }
   }
}
