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

/**
 *
 * @since 0.1
 */
public class CountryTest {

   public CountryTest() {
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
    * Test of getISO31662 method, of class Country.
    */
   @Test
   public void testUnitedStates() {
      System.out.println("CountryTest : testUnitedStates");
      Country country = Country.getCountryFromName("United States");
      assertNotNull("United States must exist", country);

      country = Country.getCountryFromName("United States of America");
      assertNotNull("United States of America must exist", country);
   }

   /**
    * Test of getISO31662 method, of class Country.
    */
   @Test
   public void testAfghanistan() {
      System.out.println("CountryTest : testAfghanistan");
      Country country = Country.getCountryFromName("Afghanistan");
      assertNotNull("Afghanistan must exist", country);
      assertEquals("Afghanistan numeric code", 4, country.getNumericCode());
      assertEquals("Afghanistan numeric code", "004", country.getFormattedNumericCode());
      assertEquals("Afghanistan ISO3166-2", "ISO 3166-2:AF", country.getISO31662());
   }

}
