package hutech.com.demo.service;

import groovyjarjarpicocli.CommandLine;
import hutech.com.demo.Helper;
import hutech.com.demo.UserHelper;
import hutech.com.demo.model.Product;
import hutech.com.demo.model.User;
import hutech.com.demo.repository.IUserRepository;
import hutech.com.demo.repository.ProductRepository;
import hutech.com.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;

    public ByteArrayInputStream getProductExcelData() throws IOException {
        List<Product> products = productRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = Helper.dataToExel(products);
        return byteArrayInputStream;
    }

    public ByteArrayInputStream getUserExcelData() throws IOException {
        List<User> users = userRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = UserHelper.dataToExel(users);
        return byteArrayInputStream;
    }


}
