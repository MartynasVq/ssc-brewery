/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.seurity.AuthorityRepository;
import guru.sfg.brewery.repositories.seurity.RoleRepository;
import guru.sfg.brewery.repositories.seurity.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final AuthorityRepository auth;
    private final UserRepository userrep;
    private final RoleRepository roleRepository;
    private final PasswordEncoder psw;

    @Override
    public void run(String... args) {
        loadBreweryData();
        loadCustomerData();
        loadUserData();
    }

    private void loadUserData() {




        if(auth.count() > 0)
            return;

        Authority createBeer = auth.save(Authority.builder().permission("beer.create").build());
        Authority updateBeer = auth.save(Authority.builder().permission("beer.update").build());
        Authority deleteBeer = auth.save(Authority.builder().permission("beer.delete").build());
        Authority readBeer = auth.save(Authority.builder().permission("beer.read").build());
        Authority createCustomer = auth.save(Authority.builder().permission("customer.create").build());
        Authority updateCustomer = auth.save(Authority.builder().permission("customer.update").build());
        Authority deleteCustomer = auth.save(Authority.builder().permission("customer.delete").build());
        Authority readCustomer = auth.save(Authority.builder().permission("customer.read").build());
        Authority createBrewery = auth.save(Authority.builder().permission("brewery.create").build());
        Authority updateBrewery = auth.save(Authority.builder().permission("brewery.update").build());
        Authority deleteBrewery = auth.save(Authority.builder().permission("brewery.delete").build());
        Authority readBrewery = auth.save(Authority.builder().permission("brewery.read").build());
        Authority createOrder = auth.save(Authority.builder().permission("order.create").build());
        Authority updateOrder = auth.save(Authority.builder().permission("order.update").build());
        Authority deleteOrder = auth.save(Authority.builder().permission("order.delete").build());
        Authority readOrder = auth.save(Authority.builder().permission("order.read").build());
        Authority createOrderCustomer = auth.save(Authority.builder().permission("customer.order.create").build());
        Authority updateOrderCustomer = auth.save(Authority.builder().permission("customer.order.update").build());
        Authority deleteOrderCustomer = auth.save(Authority.builder().permission("customer.order.delete").build());
        Authority readOrderCustomer = auth.save(Authority.builder().permission("customer.order.read").build());

        Role admin = roleRepository.save(Role.builder().name("ADMIN").build());
        Role user = roleRepository.save(Role.builder().name("USER").build());
        Role customer = roleRepository.save(Role.builder().name("CUSTOMER").build());

        admin.setAuthorities(Set.of(createBeer,deleteBeer,updateBeer,readBeer, createBrewery, deleteBrewery
        , deleteCustomer, updateBrewery, updateCustomer, createCustomer, readBrewery, readCustomer, createOrder,
                deleteOrder, updateOrder, readOrder));
        customer.setAuthorities(Set.of(readBeer, readBrewery, createOrderCustomer, deleteOrderCustomer,
                updateOrderCustomer, readOrderCustomer));
        user.setAuthorities(Set.of(readBeer));

        roleRepository.saveAll(Arrays.asList(admin, user, customer));

        User admincx = userrep.save(User.builder().username("admin2").password(psw.encode("admin2"))
        .role(admin).build());
        User usercx = userrep.save(User.builder().username("user").password(psw.encode("user"))
                .role(user).build());
        User customercx = userrep.save(User.builder().username("scott").password(psw.encode("tiger"))
                .role(customer).build());

        log.debug("Users and authorities loaded");
    }

    private void loadCustomerData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer -> {
            beerOrderRepository.save(BeerOrder.builder()
                    .customer(tastingRoom)
                    .orderStatus(OrderStatusEnum.NEW)
                    .beerOrderLines(Set.of(BeerOrderLine.builder()
                            .beer(beer)
                            .orderQuantity(2)
                            .build()))
                    .build());
        });
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }
}
