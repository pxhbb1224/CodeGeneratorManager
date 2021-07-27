package com.cmbchina.code_generator;

import com.cmbchina.code_generator.model.Attribute;
import com.cmbchina.code_generator.model.Table;
import com.cmbchina.code_generator.utils.FormatNameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeGeneratorManagerApplicationTests {

    @Test
    void contextLoads() {
        Table table1 = new Table();
        table1.addTable(new Attribute(1));
        table1.addTable(new Attribute());
        System.out.println(FormatNameUtils.formatToSql(table1));
    }

}
