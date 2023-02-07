package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;


@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


     /*목록 표시*/
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list....."+pageRequestDTO);

        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }

    /*게시물 등록*/
    @GetMapping("/register") //Get 방식으로 동작
    public void register(){
        log.info("register get....");
    }

    @PostMapping("/register") // Post 방식으로 실제 처리
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto...."+dto);;
        // 새로 추가된 엔티티 번호
        Long bno = boardService.register(dto);

        log.info("BNO : "+bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    /*게시물 조회/수정/삭제 처리*/
    @GetMapping({"/read","/modify"}) // read - 게시물 조회
    public void read(@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Long bno, Model model){
        log.info("bno : "+bno);
        BoardDTO boardDTO = boardService.get(bno);

        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/remove")  // 게시물 삭제
    public String remove(long bno, RedirectAttributes redirectAttributes){
        log.info("bno : "+bno);
        boardService.removeWithReplies(bno);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list";
    }

    @PostMapping("/modify") // 게시물 수정
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){
        log.info("post modify..............");
        log.info("dto : "+dto);

        boardService.modify(dto);

        redirectAttributes.addFlashAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addFlashAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addFlashAttribute("keyword", pageRequestDTO.getKeyword());

        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }
}
