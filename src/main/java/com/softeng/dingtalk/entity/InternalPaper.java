package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softeng.dingtalk.enums.PaperType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 论文实体类
 * @create 2/5/2020 4:33 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class InternalPaper implements Paper {
    /**
     * 论文的投稿结果
     */
    public static final int WAIT = 0;
    public static final int NOTPASS = 1;
    public static final int REVIEWING = 2;
    public static final int REJECT= 3;
    public static final int ACCEPT = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String journal;

    /**
     * 更新时间
     */
    private LocalDate updateDate;

    /**
     * 论文等级
     */
    @Enumerated(EnumType.STRING)
    private PaperType paperType;

    /**
     * 投稿结果
     */
    private int result;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private Vote vote;

    @JsonIgnoreProperties("internalPaper")
    @OneToMany(mappedBy = "internalPaper")
    private List<PaperDetail> paperDetails;

    public InternalPaper(int id) {
        this.id = id;
    }

    public InternalPaper(String title, String journal, PaperType paperType, LocalDate updateDate) {
        this.title = title;
        this.journal = journal;
        this.paperType = paperType;
        this.updateDate = updateDate;
    }

    public void update(String title, String journal, PaperType paperType, LocalDate issueDate) {
        this.title = title;
        this.journal = journal;
        this.paperType = paperType;
        this.updateDate = issueDate;
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    public boolean hasAccepted() {
        return result == ACCEPT;
    }
    public boolean hasRejected() {
        return result == REJECT;
    }
}
