import SideBar from "../components/SideBar";
import HeaderMenu from "../components/Header";
import { Routes, Route, useParams, useLocation } from 'react-router-dom';

import { Layout } from "antd";
import React, { useState } from "react";
import { Outlet } from "react-router";
const { Sider, Content, Header } = Layout;

const Dashboard: React.FC = () => {
  console.log(useParams(), useLocation());
  
  const [collapsed, setCollapsed] = useState(false);
  return (
    <Layout style={{ height: "100vh" }}>
      <Sider breakpoint="xl" collapsible>
        <SideBar />
      </Sider>
      <Layout className="site-layout">
        {!useLocation().pathname.includes('stock_transfers') && <Header className="top-header z-10" style={{ padding: 0 }}>
          <HeaderMenu />
        </Header>}
        <Content className="pt-5 pl-10 pr-10">
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

export default Dashboard;
