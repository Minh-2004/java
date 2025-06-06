import React from "react";
import {
  Image,
  Text,
  View,
  Page,
  Document,
  StyleSheet,
} from "@react-pdf/renderer";
import logo from "./img/LogoHITC.png";

const MyDocument = ({ data }) => {
  const { cartId, products } = data;

  // Calculate total price based on products array
  const calculatedTotalPrice = products.reduce((acc, product) => {
    return acc + product.price * product.quantity;
  }, 0);

  const styles = StyleSheet.create({
    page: {
      fontSize: 11,
      paddingTop: 20,
      paddingLeft: 40,
      paddingRight: 40,
      lineHeight: 1.5,
      flexDirection: "column",
    },
    spaceBetween: {
      flex: 1,
      flexDirection: "row",
      alignItems: "center",
      justifyContent: "space-between",
      color: "#3E3E3E",
    },
    titleContainer: {
      flexDirection: "row",
      marginTop: 24,
    },
    logo: {
      width: 300,
    },
    reportTitle: {
      fontSize: 16,
      textAlign: "center",
    },
    addressTitle: {
      fontSize: 15,
      fontWeight: "bold",
    },
    invoice: {
      fontWeight: "bold",
      fontSize: 20,
    },
    invoiceNumber: {
      fontSize: 11,
      fontWeight: "bold",
    },
    address: {
      fontWeight: 400,
      fontSize: 13,
    },
    theader: {
      marginTop: 20,
      fontSize: 10,
      fontWeight: "bold",
      paddingTop: 4,
      paddingLeft: 7,
      flex: 1,
      height: 20,
      backgroundColor: "#DEDEDE",
      borderColor: "whitesmoke",
      borderRightWidth: 1,
      borderBottomWidth: 1,
    },
    theader2: {
      flex: 2,
      borderRightWidth: 0,
      borderBottomWidth: 1,
    },
    tbody: {
      fontSize: 9,
      paddingTop: 4,
      paddingLeft: 7,
      flex: 1,
      borderColor: "whitesmoke",
      borderRightWidth: 1,
      borderBottomWidth: 1,
    },
    total: {
      fontSize: 9,
      paddingTop: 4,
      paddingLeft: 7,
      flex: 1.5,
      borderColor: "whitesmoke",
      borderBottomWidth: 1,
    },
  });

  const InvoiceTitle = () => (
    <View style={styles.titleContainer}>
      <View style={styles.spaceBetween}>
        <Image style={styles.logo} src={logo} />
      </View>
    </View>
  );

  const UserAddress = () => (
    <View style={styles.titleContainer}>
      <View style={styles.spaceBetween}>
        <View style={{ maxWidth: 200 }}>
          <Text style={styles.addressTitle}>
            Email:{" "}
            <Text style={styles.address}>
              {localStorage.getItem("username")}
            </Text>
          </Text>
          <Text style={styles.addressTitle}>
            Total Price:{" "}
            <Text style={styles.address}>
              {calculatedTotalPrice.toFixed(2)}
            </Text>
          </Text>
        </View>
      </View>
    </View>
  );

  const TableHead = () => (
    <View style={{ width: "100%", flexDirection: "row", marginTop: 10 }}>
      <View style={[styles.theader, { flex: 2 }]}>
        <Text>Items</Text>
      </View>
      <View style={[styles.theader, { flex: 1 }]}>
        <Text>Price</Text>
      </View>
      <View style={[styles.theader, { flex: 1 }]}>
        <Text>Qty</Text>
      </View>
      <View style={[styles.theader, { flex: 1 }]}>
        <Text>Amount</Text>
      </View>
    </View>
  );
  

  const TableBody = () =>
    products.map((product) => (
      <View
        style={{ width: "100%", flexDirection: "row" }}
        key={product.productId}
      >
        <View style={[styles.tbody, { flex: 2 }]}>
          <Text>{product.productName}</Text>
        </View>
        <View style={[styles.tbody, { flex: 1 }]}>
          <Text>{product.price.toFixed(2)}</Text>
        </View>
        <View style={[styles.tbody, { flex: 1 }]}>
          <Text>{product.quantity}</Text>
        </View>
        <View style={[styles.tbody, { flex: 1 }]}>
          <Text>{(product.price * product.quantity).toFixed(2)}</Text>
        </View>
      </View>
    ));
  

    const TableTotal = () => (
        <View style={{ width: "100%", flexDirection: "row" }}>
          <View style={[styles.total, { flex: 2 }]}>
            <Text></Text>
          </View>
          <View style={[styles.total, { flex: 1 }]}>
            <Text></Text>
          </View>
          <View style={[styles.tbody, { flex: 1 }]}>
            <Text>Total</Text>
          </View>
          <View style={[styles.tbody, { flex: 1 }]}>
            <Text>{calculatedTotalPrice.toFixed(2)}</Text>
          </View>
        </View>
      );
      

  return (
    <Document>
      <Page size="A4" style={styles.page}>
        <InvoiceTitle />
        <UserAddress />
        <TableHead />
        <TableBody />
        <TableTotal />
      </Page>
    </Document>
  );
};

export default MyDocument;
