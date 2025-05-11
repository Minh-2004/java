import { List, Datagrid, TextField, DeleteButton, EditButton, Create, Edit, SimpleForm, TextInput } from "react-admin";

// Danh sách các danh mục
export const CategoryList = () => (
    <List>
        <Datagrid>
            <TextField source="categoryId" label="Category ID" />
            <TextField source="categoryName" label="Category Name" />
            <EditButton />
            <DeleteButton />
        </Datagrid>
    </List>
);

// Tạo danh mục mới
export const CategoryCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="categoryName" label="Category Name" />
        </SimpleForm>
    </Create>
);

// Chỉnh sửa danh mục
export const CategoryEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="categoryId" label="Category ID" disabled />
            <TextInput source="categoryName" label="Category Name" />
        </SimpleForm>
    </Edit>
);
