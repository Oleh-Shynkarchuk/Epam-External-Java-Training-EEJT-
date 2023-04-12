package com.epam.esm.certificate.graphqlutils;

public class GraphqlUtils {
    private GraphqlUtils() {
    }

    public static final String CERTIFICATE_BY_TAG_ID_QUERY = """
            query($filter:String)
              {
                  products(where: $filter) {
                      results {
                          id
                          lastModifiedAt
                          createdAt
                          masterData
                      {
                          current{
                              name(locale:"en-US")
                              description(locale:"en-US")
                              categories{
                                  id
                              }
                              allVariants{
                                  id
                                  sku
                                  key
                                  prices{
                                      value{
                                          centAmount
                                          currencyCode
                                      }
                                      country
                                      id
                                  },
                                  attributesRaw{
                                       name
                                       value
                                  }
                              },
                          }
                      }
                      version
                  }
              }
              }
            """;
    public static final String CATEGORIES_WHERE_TAGS_NAME = """
            query($filter:String) {
                categories(where: $filter) {
                    results {
                     id name(locale: "en-US")
                    }
                }
            }
            """;

}
