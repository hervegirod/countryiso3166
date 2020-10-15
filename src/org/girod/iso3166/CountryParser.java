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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Represent one ISO 3166-1 Country.
 *
 * @since 0.1
 */
class CountryParser extends DefaultHandler {
   private Country.Cache cache = null;
   private String name = null;
   private String alpha2 = null;
   private String alpha3 = null;
   private int numeric = 0;
   private List<String> altNames = null;

   CountryParser(Country.Cache cache) {
      this.cache = cache;
   }

   void parseSpec() {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      try {
         SAXParser parser = factory.newSAXParser();
         InputStream stream = this.getClass().getResourceAsStream("countries.xml");
         parser.parse(stream, this);
      } catch (ParserConfigurationException | SAXException | IOException ex) {
      }
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
      if (qName.equals("country")) {
         parseCountry(attrs);
      } else if (qName.equals("altName")) {
         parseAltName(attrs);
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (qName.equals("country")) {
         addCountry();
      }
   }

   private void addCountry() {
      if (name != null && alpha2 != null && alpha3 != null && numeric != 0) {
         Country country = new Country(name, altNames, alpha2, alpha3, numeric, false);
         addCountryToCache(country, altNames);
      }
   }

   private void parseAltName(Attributes attr) {
      String altName = null;

      for (int i = 0; i < attr.getLength(); i++) {
         String attrname = attr.getQName(i);
         String attrvalue = attr.getValue(i);

         if (attrname.equals("name")) {
            altName = attrvalue;
         }
      }
      if (altName != null) {
         if (altNames == null) {
            altNames = new ArrayList<>();
         }
         altNames.add(altName);
      }
   }

   private void parseCountry(Attributes attr) {
      name = null;
      alpha2 = null;
      alpha3 = null;
      numeric = 0;
      altNames = null;

      for (int i = 0; i < attr.getLength(); i++) {
         String attrname = attr.getQName(i);
         String attrvalue = attr.getValue(i);

         if (attrname.equals("name")) {
            name = attrvalue;
         } else if (attrname.equals("alpha2")) {
            alpha2 = attrvalue;
         } else if (attrname.equals("alpha3")) {
            alpha3 = attrvalue;
         } else if (attrname.equals("numeric")) {
            try {
               numeric = Integer.parseInt(attrvalue);
            } catch (NumberFormatException e) {
            }
         }
      }
   }

   private void addCountryToCache(Country country) {
      cache.countryByName.put(country.getName(), country);
      cache.countryByAlpha2.put(country.getAlpha2(), country);
      cache.countryByAlpha3.put(country.getAlpha3(), country);
      cache.countryByNum.put(country.getNumericCode(), country);
   }

   private void addCountryToCache(Country country, List<String> altNames) {
      addCountryToCache(country);
      if (altNames != null) {
         Iterator<String> it = altNames.iterator();
         while (it.hasNext()) {
            String altName = it.next();
            cache.countryByName.put(altName, country);
         }
      }
   }
}
