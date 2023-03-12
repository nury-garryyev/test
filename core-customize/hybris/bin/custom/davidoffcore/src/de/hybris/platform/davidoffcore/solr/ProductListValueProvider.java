package de.hybris.platform.davidoffcore.solr;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.CategoryCodeValueProvider;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.davidoffcore.model.ProductListCategoryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import org.springframework.beans.factory.annotation.Required;


import java.util.ArrayList;
import java.util.Collection;


public class ProductListValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

    private FlexibleSearchService flexibleSearchService;

    private FieldNameProvider fieldNameProvider;

    private final String DEFAULT_COUNTRY_ISO_CODE = "PL";

    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider) {
        this.fieldNameProvider = fieldNameProvider;
    }

    private Collection<ProductListCategoryModel> getProductListCategories(ProductModel product, String countryIsoCode) {

        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(
                " SELECT {cat.pk} FROM " +
                " { Product as prod " +
                " JOIN VariantProduct as var1 on {var1.baseProduct} = {prod.pk} " +
                " JOIN VariantProduct as var2 on {var2.baseProduct} = {var1.pk} " +
                " JOIN CategoryProductRelation as catProdRel on {catProdRel.target} = {var2.pk} " +
                " JOIN ProductListCategory as cat on {cat.pk} = {catProdRel.source} " +
                " JOIN Country as c on {cat.country} = {c.pk} AND {c.isocode} = ?countryIsoCode }" +
                " WHERE {prod.pk} = ?product"
        );
        flexibleSearchQuery.addQueryParameter("countryIsoCode", countryIsoCode);
        flexibleSearchQuery.addQueryParameter("product", product);
        SearchResult<ProductListCategoryModel> result = flexibleSearchService.search(flexibleSearchQuery);


        return result.getResult();
    }

    @Override
    public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final Object model) throws FieldValueProviderException
    {

        if (model instanceof ProductModel)
        {
            final ProductModel productModel = (ProductModel) model;

            String countryIsoCode = ValueProviderParameterUtils.getString(indexedProperty, "countryIsoCode", DEFAULT_COUNTRY_ISO_CODE);

            final Collection<FieldValue> fieldValues = new ArrayList<>();

            final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);

            getProductListCategories(productModel, countryIsoCode)
                    .forEach(productList ->
                        fieldNames.forEach(name ->
                                fieldValues.add(new FieldValue(name, productList.getCode())))
                    );

            return fieldValues;
        }
        else
        {
            throw new FieldValueProviderException("Error message!");
        }
    }

}
