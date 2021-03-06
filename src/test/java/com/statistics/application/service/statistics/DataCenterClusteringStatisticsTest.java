package com.statistics.application.service.statistics;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.statistics.domain.CloudInstance;
import com.statistics.domain.Customer;
import com.statistics.domain.Host;
import static org.junit.Assert.assertEquals;

public class DataCenterClusteringStatisticsTest {

    @Test
    public void shouldCustomerMaximumOfFleetPerDataCenter() throws Exception {

        //given
        CloudInstance instanceOne = CloudInstance.from("1", "8", "2");
        CloudInstance instanceTwo = CloudInstance.from("2", "8", "2");
        CloudInstance instanceThree = CloudInstance.from("3", "8", "2");
        CloudInstance instanceFour = CloudInstance.from("4", "8", "7");
        CloudInstance instanceTwelve = CloudInstance.from("12", "8", "6");

        CloudInstance instanceEight = CloudInstance.from("8", "9", "3");
        CloudInstance instanceFourteen = CloudInstance.from("14", "9", "9");
        CloudInstance instanceFifteen = CloudInstance.from("15", "9", "7");

        CloudInstance instanceSeven = CloudInstance.from("7", "13", "9");
        CloudInstance instanceNine = CloudInstance.from("9", "13", "3");

        CloudInstance instanceFive = CloudInstance.from("5", "15", "7");
        CloudInstance instanceEleven = CloudInstance.from("11", "15", "8");

        CloudInstance instanceSix = CloudInstance.from("6", "16", "9");
        CloudInstance instanceTen = CloudInstance.from("10", "16", "5");
        CloudInstance instanceThirteen = CloudInstance.from("13", "16", "8");

        Customer customerEight = Customer.from("8", Arrays.asList(instanceOne, instanceTwo, instanceThree, instanceFour, instanceTwelve)); // 4/5
        Customer customerFifteen = Customer.from("15", Arrays.asList(instanceFive, instanceEleven)); //1/2
        Customer customerSixteen = Customer.from("16", Arrays.asList(instanceSix, instanceTen, instanceThirteen)); //1/3

        List<Customer> customers = Arrays.asList(customerFifteen, customerSixteen, customerEight);

        Host hostTwo = Host.from("2", 4, "0", Arrays.asList(instanceOne, instanceTwo, instanceThree));
        Host hostFive = Host.from("5", 4, "0", Collections.singletonList(instanceTen));
        Host hostSeven = Host.from("7", 3, "0", Arrays.asList(instanceFour, instanceFive, instanceFifteen));
        Host hostNine = Host.from("9", 3, "1", Arrays.asList(instanceSix, instanceSeven, instanceFourteen));
        Host hostThree = Host.from("3", 3, "1", Arrays.asList(instanceEight, instanceNine));
        Host hostTen = Host.from("10", 2, "2", Collections.emptyList());
        Host hostSix = Host.from("6", 4, "2", Collections.singletonList(instanceTwelve));
        Host hostEight = Host.from("8", 2, "2", Arrays.asList(instanceEleven, instanceThirteen));

        List<Host> hosts = Arrays.asList(hostTwo, hostFive, hostSeven, hostNine, hostThree, hostTen, hostSix,
                hostEight);
        //when
        Collection<Map.Entry<Customer, Double>> customerDoubleEntry = new DataCenterClusteringStatistics(customers, hosts).customerMaximumOfFleetPerDataCenter();

        //then
        AbstractMap.SimpleEntry<Customer, Double> expected = new AbstractMap.SimpleEntry<>(customerEight, 0.8);
        assertEquals(1, customerDoubleEntry.size());
        assertEquals(expected, customerDoubleEntry.iterator().next());

    }


    @Test
    public void shouldReturnAllCustomerMaximumOfFleetPerDataCenter() throws Exception {

        //given

        CloudInstance instanceFive = CloudInstance.from("5", "15", "7");
        CloudInstance instanceEleven = CloudInstance.from("11", "15", "8");
        CloudInstance instanceOne = CloudInstance.from("1", "15", "9");

        CloudInstance instanceSix = CloudInstance.from("6", "16", "9");
        CloudInstance instanceTen = CloudInstance.from("10", "16", "5");
        CloudInstance instanceThirteen = CloudInstance.from("13", "16", "8");


        Customer customerFifteen = Customer.from("15", Arrays.asList(instanceFive, instanceEleven, instanceOne)); //1/3
        Customer customerSixteen = Customer.from("16", Arrays.asList(instanceSix, instanceTen, instanceThirteen)); //1/3

        List<Customer> customers = Arrays.asList(customerFifteen, customerSixteen);

        Host hostFive = Host.from("5", 4, "0", Collections.singletonList(instanceTen));
        Host hostSeven = Host.from("7", 3, "0", Collections.singletonList(instanceFive));
        Host hostNine = Host.from("9", 3, "1", Arrays.asList(instanceSix, instanceOne));
        Host hostThree = Host.from("3", 3, "1", Collections.emptyList());
        Host hostTen = Host.from("10", 2, "2", Collections.emptyList());
        Host hostSix = Host.from("6", 4, "2", Collections.emptyList());
        Host hostEight = Host.from("8", 2, "2", Arrays.asList(instanceEleven, instanceThirteen));

        List<Host> hosts = Arrays.asList(hostFive, hostSeven, hostNine, hostThree, hostTen, hostSix,
                hostEight);
        //when
        Collection<Map.Entry<Customer, Double>> customerDoubleEntry = new DataCenterClusteringStatistics(customers, hosts).customerMaximumOfFleetPerDataCenter();
        System.out.print(customerDoubleEntry);
        //then
        AbstractMap.SimpleEntry<Customer, Double> expectedOne = new AbstractMap.SimpleEntry<>(customerFifteen, 0.3333333333333333);
        AbstractMap.SimpleEntry<Customer, Double> expectedTwo = new AbstractMap.SimpleEntry<>(customerSixteen, 0.3333333333333333);
        final List<AbstractMap.SimpleEntry<Customer, Double>> expected = Arrays.asList(expectedOne, expectedTwo);

        assertEquals(2, customerDoubleEntry.size());
        int count = 0;
        Iterator<Map.Entry<Customer, Double>> iterator = customerDoubleEntry.iterator();
        while (iterator.hasNext()) {
            assertEquals(expected.get(count), iterator.next());
            count ++;
            iterator.next();
        }

    }
}