package org.zerock.guestbook.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@Log4j2
@RequestMapping("/guestbook")
@RequiredArgsConstructor // 자동 주입 위한 Annotation
public class GuestbookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list..................."+pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

//    @GetMapping("/list")
//    public void list(PageRequestDTO pageRequestDTO, Model model){
//        log.info("List..................."+pageRequestDTO);
//        model.addAttribute("result", service.getList(pageRequestDTO));
//    }

    /*등록 입력 및 처리 후 이동*/
    @GetMapping("/register")// 화면 표시
    public void register(){
        log.info("register get...");
    }

    @PostMapping("/register") // 처리 후 목록으로 이동
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){ // redirectAttribute-> 한 번만 데이터 전달(메세지 전달)
        log.info("dto....."+dto);

        // 새로 추가된 엔티티 번호
        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg",gno);

        return "redirect:/guestbook/list";
    }

    /* 방명록 조회 */
//    @GetMapping("/read")
//    public void read(long gno, @ModelAttribute("requestDTO") PageResultDTO requestDTO, Model model){
//        log.info("gno: "+gno);
//        GuestbookDTO dto = service.read(gno);
//        model.addAttribute("dto",dto);
//    }

    @GetMapping("/read")
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno : "+gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    }
}
