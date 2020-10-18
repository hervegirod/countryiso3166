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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Used to check the validty of the XML file for countries.
 *
 * @since 0.2
 */
public class TestCountriesParser extends DefaultHandler {
   private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   private String name = null;
   private String alpha2 = null;
   private String alpha3 = null;
   private int numeric = 0;
   private List<String> altNames = null;
   private Locator locator = null;
   private final Set<String> byName = new HashSet<>();
   private final Set<String> byAlpha2 = new HashSet<>();
   private final Set<String> byAlpha3 = new HashSet<>();
   private final Set<Integer> byNumeric = new HashSet<>();
   private final List<SAXParseException> errors = new ArrayList<>();

   public TestCountriesParser() {
   }

   /**
    * Return the document locator. Note that by default the locator used by this class is null.
    */
   @Override
   public void setDocumentLocator(Locator locator) {
      this.locator = locator;
   }

   public List<SAXParseException> getErrors() {
      return errors;
   }

   public void parseSpec() {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      try {
         SAXParser parser = factory.newSAXParser();
         parser.setProperty(JAXP_SCHEMA_LANGUAGE, XMLConstants.W3C_XML_SCHEMA_NS_URI);
         factory.setValidating(true);
         factory.setFeature("http://apache.org/xml/features/validation/schema", true);
         SchemaFactory schemaFac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
         URL schemaURL = this.getClass().getResource("countries.xsd");
         Schema schema = schemaFac.newSchema(schemaURL);

         Validator validator = schema.newValidator();
         validator.setErrorHandler(this);
         InputStream stream = this.getClass().getResourceAsStream("countries.xml");
         InputSource source = new InputSource(stream);
         SAXSource saxSource = new SAXSource(source);
         validator.validate(saxSource);

         factory.setSchema(schema);
         stream = this.getClass().getResourceAsStream("countries.xml");
         parser.parse(stream, this);
      } catch (ParserConfigurationException | SAXException | IOException ex) {
      }
   }

   @Override
   public void error(SAXParseException e) {
      System.err.println(e.getMessage());
      errors.add(e);
   }

   @Override
   public void warning(SAXParseException e) {
      System.err.println(e.getMessage());
      errors.add(e);
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

   private void errorImpl(String message) {
      error(new SAXParseException(message, locator));
   }

   private void addCountry() {
      if (name != null && alpha2 != null && alpha3 != null && numeric != 0) {
         if (byName.contains(name)) {
            errorImpl("Country of name " + name + " already exists");
         }
         byName.add(name);
         if (byAlpha2.contains(alpha2)) {
            errorImpl("Country of alpha2 " + alpha2 + " already exists");
         }
         byAlpha2.add(alpha2);
         if (byAlpha3.contains(alpha3)) {
            errorImpl("Country of alpha2 " + alpha3 + " already exists");
         }
         byAlpha3.add(alpha3);
         if (byNumeric.contains(numeric)) {
            errorImpl("Country of numeric code " + numeric + " already exists");
         }
         byNumeric.add(numeric);
      } else {
         errorImpl("Country of name " + name + " definition incorect");
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
         if (byName.contains(altName)) {
            errorImpl("Country of name " + altName + " already exists");
         }
         byName.add(altName);
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
}
