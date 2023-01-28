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
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno : "+gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    }

    // 게시물 삭제
    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){

        log.info("gno: "+gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg",gno);
        return "redirect:/guestbook/list";
    }

    // 게시물 수정
    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){
        // GuestbookDTO - 수정해야 하는 글의 정보
        // PageRequestDTO - 기존 페이지 정보 유지
        // RedirectAttribute - 리다이렉트로 이동


        log.info("post modify......................");
        log.info("dto : "+dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("gno", dto.getGno());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("gno", requestDTO.getKeyword());

        return "redirect:/guestbook/read";
    }
}
