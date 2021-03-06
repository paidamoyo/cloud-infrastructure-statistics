package com.statistics.application.service.statistics;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.statistics.domain.CloudInstance;
import com.statistics.domain.Customer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HostClusteringStatisticsTest {

    @Test
    public void shouldReturnAMapOfCustomerMaxNumberOfFleetPerHost() throws Exception {

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

        Customer customerEight = Customer.from("8", Arrays.asList(instanceOne, instanceTwo, instanceThree, instanceFour, instanceTwelve));// 3/5
        Customer customerNine = Customer.from("9", Arrays.asList(instanceEight, instanceFourteen, instanceFifteen));//1/3
        Customer customerThirteen = Customer.from("13", Arrays.asList(instanceSeven, instanceNine));//1/2
        Customer customerFifteen = Customer.from("15", Arrays.asList(instanceFive, instanceEleven)); //1/2
        Customer customerSixteen = Customer.from("16", Arrays.asList(instanceSix, instanceTen, instanceThirteen));//1/3

        List<Customer> customers = Arrays.asList(customerThirteen, customerFifteen, customerSixteen, customerEight, customerNine);
        HostClusteringStatistics hostClusteringStatistics = new HostClusteringStatistics(customers);

        //when
        Set<Map.Entry<Customer, Double>> customerMaxFleetOnHost = hostClusteringStatistics.customerMaximumOfFleetPerHost();

        //then
        Set<Map.Entry<Customer, Double>> expected = new HashSet<>();
        expected.add(new AbstractMap.SimpleEntry<>(customerEight, 0.6));
        assertThat(customerMaxFleetOnHost.size(), is(1));
        assertThat(customerMaxFleetOnHost, is(expected));
    }

    @Test
    public void shouldReturnMultipleEntriesWithSameMaximum() throws Exception {

        //given
        CloudInstance instanceOne = CloudInstance.from("1", "8", "2");
        CloudInstance instanceTwo = CloudInstance.from("2", "8", "2");
        CloudInstance instanceThree = CloudInstance.from("3", "8", "2");
        CloudInstance instanceFour = CloudInstance.from("4", "8", "7");
        CloudInstance instanceTwelve = CloudInstance.from("12", "8", "6");

        CloudInstance instanceEight = CloudInstance.from("8", "9", "3");
        CloudInstance instanceSix = CloudInstance.from("6", "9", "3");
        CloudInstance instanceSeven = CloudInstance.from("7", "9", "3");
        CloudInstance instanceFourteen = CloudInstance.from("14", "9", "9");
        CloudInstance instanceFifteen = CloudInstance.from("15", "9", "7");
        ;

        Customer customerEight = Customer.from("8", Arrays.asList(instanceOne, instanceTwo, instanceThree, instanceFour, instanceTwelve));
        Customer customerNine = Customer.from("9", Arrays.asList(instanceEight, instanceSix, instanceSeven, instanceFourteen, instanceFifteen));


        List<Customer> customers = Arrays.asList(customerEight, customerNine);

        HostClusteringStatistics hostClusteringStatistics = new HostClusteringStatistics(customers);

        //when
        Set<Map.Entry<Customer, Double>> customerMaxFleetOnHost = hostClusteringStatistics.customerMaximumOfFleetPerHost();

        //then
        Set<Map.Entry<Customer, Double>> expected = new HashSet<>();
        expected.add(new AbstractMap.SimpleEntry<>(customerEight, 0.6));
        expected.add(new AbstractMap.SimpleEntry<>(customerNine, 0.6));
        assertThat(customerMaxFleetOnHost.size(), is(2));
        assertThat(customerMaxFleetOnHost, is(expected));
    }
}