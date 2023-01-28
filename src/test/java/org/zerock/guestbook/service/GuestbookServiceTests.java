package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {
    @Autowired
    private GuestbookService service;

    @Test // GuestbookDTO를 entity로 변환(코드 반영X)
    public void testRegister(){

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample title!")
                .content("Sample Content!!")
                .writer("User0")
                .build();
        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO <GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        for(GuestbookDTO guestbookDTO:resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }
    }

    @Test
    public void testList2(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV : "+resultDTO.isPrev());
        System.out.println("NEXT : "+resultDTO.isNext());
        System.out.println("TOTAL : "+resultDTO.getTotalPage());
        System.out.println("-".repeat(50));

        for(GuestbookDTO guestbookDTO: resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }

        System.out.println("-".repeat(50));
        resultDTO.getPageList().forEach(i -> System.out.println(i));

    }
}
