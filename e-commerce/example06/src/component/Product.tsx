import { List, useRecordContext, Link, Datagrid, TextField, ImageField, NumberField, Create, Edit, SimpleForm, TextInput, NumberInput, ImageInput, ReferenceInput, SelectInput, EditButton, DeleteButton } from 'react-admin';
import { Link as RouterLink } from 'react-router-dom';
import { Box } from '@mui/material';


// CustomImageField Component
const CustomImageField = ({ source }: { source: string }) => {
  const record = useRecordContext();

  if (!record || !record[source]) {
    return <span>No Image</span>;
  }

  return (
    <RouterLink to={`/products/${record.id}/update-image`}>
      <img
        src={record[source]}
        alt="Product"
        style={{ width: '100px', height: 'auto' }}
      />
    </RouterLink>
  );
};

// Post Filters for Product List
const postFilters = [
  <TextInput key="search" source="search" label="Search" alwaysOn />,
  <ReferenceInput key="category" source="categoryId" reference="categories" label="Category">
    <SelectInput optionText="categoryName" />
  </ReferenceInput>,
];

// Product List Component
export const ProductList = () => (
  <List filters={postFilters}>
    <>
      {/* Nút chuyển sang trang so sánh sản phẩm */}
      <Box sx={{ p: 2 }}>
        <RouterLink to="/compare">🔍 So sánh sản phẩm</RouterLink>
      </Box>

      <Datagrid rowClick={false}>
        <TextField source="productId" label="Product ID" />
        <TextField source="productName" label="Product Name" />
        <CustomImageField source="image" />
        <TextField source="description" label="Description" />
        <NumberField source="quantity" label="Quantity" />
        <NumberField source="price" label="Price" />
        <NumberField source="discount" label="Discount" />
        <NumberField source="specialPrice" label="Special Price" />
        <EditButton />
        <DeleteButton />
      </Datagrid>
    </>
  </List>
);
export const ProductCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="productName" label="Product Name (Product name must contain atleast 3 charaters)" />
      <TextInput source="description" label="Description (Product Description must contain atleast 6 charaters)" />
      <NumberInput source="quantity" label="Quantity" />
      <NumberInput source="price" label="price" />
      <NumberInput source="discount" label="Discount" />
      <NumberInput source="specialPrice" label="specialPrice" />
      <ReferenceInput source="categoryId" reference="categories" label="Category">
        <SelectInput optionText="categoryName" />
      </ReferenceInput>
    </SimpleForm>
  </Create>
);
export const ProductEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="productId" disabled />
      <ReferenceInput source="categoryId" reference="categories" label="Category">
        <SelectInput optionText="categoryName" />
      </ReferenceInput>
      <TextInput source="productName" />
      <TextInput source="image" disabled />
      <TextInput source="description" />
      <NumberInput source="quantity" />
      <NumberInput source="price" />
      <NumberInput source="discount" />
      <NumberInput source="specialPrice" />
    </SimpleForm>
  </Edit>
);
