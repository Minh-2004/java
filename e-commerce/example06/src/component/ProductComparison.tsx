// ProductComparison.tsx
import { useState, useEffect } from 'react';
import { useDataProvider } from 'react-admin';

const ProductComparison = () => {
  const [product1Id, setProduct1Id] = useState('');
  const [product2Id, setProduct2Id] = useState('');
  const [product1, setProduct1] = useState<any>(null);
  const [product2, setProduct2] = useState<any>(null);
  const dataProvider = useDataProvider();

  useEffect(() => {
    if (product1Id) {
      dataProvider.getOne('products', { id: product1Id }).then(({ data }) => {
        setProduct1(data);
      });
    }
    if (product2Id) {
      dataProvider.getOne('products', { id: product2Id }).then(({ data }) => {
        setProduct2(data);
      });
    }
  }, [product1Id, product2Id, dataProvider]);

  return (
    <div style={{ padding: 20 }}>
      <h2>🔍 So sánh giá sản phẩm</h2>
      <div>
        <label>Chọn sản phẩm 1:&nbsp;</label>
        <input type="number" value={product1Id} onChange={(e) => setProduct1Id(e.target.value)} />
      </div>
      <div>
        <label>Chọn sản phẩm 2:&nbsp;</label>
        <input type="number" value={product2Id} onChange={(e) => setProduct2Id(e.target.value)} />
      </div>

      <hr />
      <div style={{ display: 'flex', gap: 40 }}>
        {product1 && (
          <div>
            <h3>{product1.productName}</h3>
            <img src={product1.image} alt="" width="150" />
            <p>Giá: {product1.price}₫</p>
            <p>Giảm giá: {product1.discount}%</p>
            <p>Giá sau giảm: {product1.specialPrice}₫</p>
          </div>
        )}
        {product2 && (
          <div>
            <h3>{product2.productName}</h3>
            <img src={product2.image} alt="" width="150" />
            <p>Giá: {product2.price}₫</p>
            <p>Giảm giá: {product2.discount}%</p>
            <p>Giá sau giảm: {product2.specialPrice}₫</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProductComparison;
