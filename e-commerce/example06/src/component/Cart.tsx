import { List, useRedirect, useNotify, useRefresh, useRecordContext, ReferenceField, TextField, Show, SimpleShowLayout, NumberField, ArrayField, ImageField, Datagrid, Identifier } from 'react-admin';
import PDFButton from './PDFButton';

const CustomPDFButton = () => {
    const record = useRecordContext();
    if (!record || !record.id) {
        return <span>Loading...</span>;
    }
    return <PDFButton />;
};

export const CartList = () => {
    const redirect = useRedirect();
    const handleRowClick = (id: Identifier | undefined, resource: string | undefined, record: { email: string; }) => {
        if (id) {
            localStorage.setItem('globalCartId', id.toString());
        }
        localStorage.setItem("globalEmailCart", record.email);
        redirect('show', resource, id);
    };

    return (
        <List>
            <Datagrid rowClick={handleRowClick}>
                <TextField source="cartId" label="Cart ID" />
                <NumberField source="totalPrice" label="Total Price" />
                {/* <TextField source="email" label="Email" /> */}
            </Datagrid>
        </List>
    );
};

export const CartShow = () => {
    // const notify = useNotify();
    // const refresh = useRefresh();
    // const redirect = useRedirect();

    // const onError = (error: { message: any; }) => {
    //     notify(`Could not load cart: ${error.message}`, { type: 'error' });
    //     redirect('/carts');
    //     refresh();
    // };

    // if (!localStorage.getItem("jwt-token")) {
    //     console.error('No JWT token found. Redirecting to login page.');
    //     window.location.href = '/login';
    //     return <span>Error: No JWT token found</span>;
    // }

    return (
        <Show> 
        <SimpleShowLayout> 
        <CustomPDFButton /> 
        <TextField source="cartId" label="Cart ID" /> 
        <NumberField source="totalPrice" label="Total Price" /> 
        <ArrayField source="products" label="Products"> 
        <Datagrid> 
        <TextField source="productId" label="Product ID" /> 
        <TextField source="productName" label="Product Name" /> 
        <ImageField source="image" label="Image" /> 
        <TextField source="description" label="Description" /> 
        <NumberField source="quantity" label="Quantity" /> 
        <NumberField source="price" label="Price" /> 
        <NumberField source="discount" label="Discount" /> 
        <NumberField source="specialPrice" label="Special Price" /> 
        <ReferenceField source="category.categoryId" reference="categories" label="Category"> 
        <TextField source="categoryName" /> 
        </ReferenceField> 
        </Datagrid> 
        </ArrayField> 
        </SimpleShowLayout> 
        </Show>
    );
};