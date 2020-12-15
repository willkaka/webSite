package com.hyw.webSite.dto;

import lombok.Data;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.FooterLine;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GitCommitInfoDto {

    private String commitId;
    private PersonIdent authorIdent;
    private PersonIdent committerIdent;
    private LocalDateTime commitDateTime;
    private Charset encoding;
    private String encodingName;
    private List<FooterLine> footerLines;
    private List<String> footerKeyList;

    private String fullMessage;
    private RevCommit parent;
    private int parentCount;
    private RevCommit[] parents;

    private byte[] rawBuffer;
    private byte[] rawGpgSignature;
    private String shortMessage;
    private RevTree tree;
    private int type;

    private List<DiffEntry> diffEntryList;
}
