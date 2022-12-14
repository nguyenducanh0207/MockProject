import React, {useEffect, useState} from "react";
import {getDistrict, getProvince, getWard} from "../services/api";
import {Col, Form, Input, Row, Select} from "antd";
type AddressType = {
    code: number,
    name: string
}
type AddressProps ={
    onChange : (str:string) => void,
    keyChange: number
}
const AddAddress = ({onChange,keyChange} : AddressProps) =>{
    const {Option} = Select
    const onChangeProvinces = (value: string) => {
        setProvinceCode(value)
        const item = provinces.find((p) => {
            return p.code.toString() == value
        })
        item && setProvinceName(", "+ item.name +", ")
    };
    const onChangeDistrict = (value: string) => {
        setDistrictCode(value)
        const item = districts.find((d) => {
            return d.code.toString() == value
        })
        item && setDistrictName(item.name  +", ")
    };
    const onChangeWard = (value: string) => {
        const item = wards.find((w) => {
            return w.code.toString() == value
        })
        item && setWardName(item.name)
    };
    const [provinces, setProvinces] = useState([{} as AddressType])
    const [districts, setDistricts] = useState([{} as AddressType])
    const [wards, setWards] = useState([{} as AddressType])

    const [provinceName, setProvinceName] = useState<string>("")
    const [districtName, setDistrictName] = useState<string>("")
    const [wardName, setWardName] = useState<string>("")

    const [provinceCode, setProvinceCode] = useState<string>()
    const [districtCode, setDistrictCode] = useState<string>()
    const [detailAddress,setDetailAddress] = useState<string>("")


    let address = detailAddress +  provinceName + districtName + wardName

    useEffect(() =>{
        onChange(address)
    },[address])

    useEffect(() =>{
        setDistricts([])
        setWards([])
        setDetailAddress("")
        setWardName("")
        setDistrictName("")
        setProvinceName("")
        address =''
    },[keyChange])

    useEffect(() => {
        getProvince().then((p) => {
            setProvinces(p.data)
        })
    }, [])

    useEffect(() => {
        provinceCode && getDistrict(provinceCode as string).then((d) => {
            setDistricts(d.data.districts)
        })
    }, [provinceCode])

    useEffect(() => {
        districtCode && getWard(districtCode as string).then((w) => {
            setWards(w.data.wards)
        })
    }, [districtCode])

    return(
        <>
            <Row gutter={24}>
                <Col span={12}>
                    <Form.Item label="?????a ch??? chi ti???t" name="detailsAddress" rules={[{required: true, message:"?????a ch??? kh??ng ???????c ????? tr???ng"}]}>
                        <Input onChange={(e) => setDetailAddress(e.target.value)} placeholder="nh???p ?????a ch??? nh?? cung c???p"/>
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item label="Th??nh ph???/T???nh" name="province" rules={[{required: true, message:"Th??nh ph??? kh??ng ???????c ????? tr???ng"}]}>
                        <Select
                            showSearch
                            placeholder="Ch???n t???nh th??nh ph???"
                            optionFilterProp="children"
                            onChange={onChangeProvinces}
                            // onSearch={onSearch}
                            listItemHeight={1} listHeight={250}
                            filterOption={(input, option) =>
                                (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
                            }
                            dropdownStyle={{height: 250, width: 100}}
                        >
                            {
                                provinces && provinces.map((p, key) => (
                                    <Option key={key} style={{width: 400}}
                                            value={p.code}>{p.name}</Option>
                                ))
                            }
                        </Select>
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item label="Qu???n/Huy???n" name="district" rules={[{required: true, message:"Qu???n huy???n kh??ng ???????c ????? tr???ng"}]}>
                        <Select
                            showSearch
                            placeholder="Ch???n qu???n huy???n"
                            optionFilterProp="children"
                            onChange={onChangeDistrict}
                            listItemHeight={1} listHeight={250}
                            filterOption={(input, option) =>
                                (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
                            }
                            // dropdownStyle={{height: 250, width: 100}}
                            dropdownMatchSelectWidth
                        >
                            {
                                districts.length > 1 ? (
                                    districts.map((d, key) => (
                                        <Option key={key} style={{width: 400}}
                                                value={d.code}>{d.name}</Option>
                                    ))
                                ) : (<Option style={{width: 400}}
                                             value="default">Ch???n qu???n huy???n</Option>)
                            }
                        </Select>
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item label="Ph?????ng/X??" name="ward" rules={[{required: true, message:"Ph?????ng x?? kh??ng ???????c ????? tr???ng"}]}>
                        <Select
                            showSearch
                            placeholder="Ch???n x?? ph?????ng"
                            optionFilterProp="children"
                            onChange={onChangeWard}
                            listItemHeight={1} listHeight={250}
                            filterOption={(input, option) =>
                                (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
                            }
                            dropdownStyle={{height: 250, width: 100}}
                        >
                            {
                                wards.length > 1 ? (
                                    wards.map((w, key) => (
                                        <Option key={key} style={{width: 400}}
                                                value={w.code}>{w.name}</Option>
                                    ))
                                ) : (<Option style={{width: 400}}
                                             value="default">Ch???n x?? ph?????ng</Option>)
                            }
                        </Select>
                    </Form.Item>
                </Col>
            </Row>
        </>
    )
}
export default React.memo(AddAddress)