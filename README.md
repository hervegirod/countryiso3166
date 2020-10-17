# countryiso3166
A Java library allowing to get the country codes. See https://en.wikipedia.org/wiki/ISO_3166-1

# Usage
## Get a Country
You can get a country by it's name, its alpha-2 code, it's alpha-3 code, or it's numeric code
- By the name: Country.getCountryFromName(String name)
- By the alpha2 code: Country.getCountryFromAlpha2(String alpha2)
- By the alpha3 code: Country.getCountryFromAlpha3(String alpha3)
- By the numeric code: Country.getCountryFromNumeric(int numeric) or Country.getCountryFromNumeric(String numeric)

## Get the properties of a Country
For a Country, the following properties can be retrieved:
- getName(): to get the country name
- getAlpha2(): to get the alpha-2 code
- getAlpha3(): to get the alpha-3 code
- getISO31662(): to get the ISO3166-2 code (constitued with the alpha-2 code concatenated with the "ISO 3166-2:" prefix)
- getNumericCode(): to get the country numeric code as an int
- getFormattedNumericCode(): to get the country numeric code as a formatted 3 characters code

## Alternate names for a country
Some countries may have alternate names. This notion does not exist in the ISO 3166-1 standard. But it helps to have 
shorter names for countries. For example "United Kingdom" for "United Kingdom of Great Britain and Northern Ireland". It is 
possible to get the alternate names for a country by the getAlternateNames() method.

## Get the list of supported Countries
The Country.listCountries() return the list of supported countries

## Adding a Country
A custom country code can be added by simply using of the the constructors.
