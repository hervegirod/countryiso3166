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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.xml.sax.SAXParseException;

/**
 * Used to check the validty of the XML file for countries.
 *
 * @since 0.2
 */
public class CountriesListTest {

   public CountriesListTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {
   }

   @After
   public void tearDown() {
   }

   /**
    * Check the validty of the XML file for countries.
    */
   @Test
   public void testCountries() {
      System.out.println("CountriesListTest : testCountries");
      TestCountriesParser parser = new TestCountriesParser();
      parser.parseSpec();
      List<SAXParseException> errors = parser.getErrors();
      assertTrue("Must not have any error", errors.isEmpty());
   }

}
