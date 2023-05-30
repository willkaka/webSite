package com.hyw.webSite.service;

import com.hyw.webSite.exception.BizException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JGit {

    public static void main(String[] args) throws Exception {

        JGit test = new JGit();
        test.getRemoteBranchs("http://gitlab.dashuf.com/ds2/caes.git","huangyuanwei","Dashuf0705");

    }

    public Git getGit(String url,String userName,String password,String branchName){
        if(StringUtils.isBlank(url) ||
                StringUtils.isBlank(userName) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(branchName)){
            throw new BizException("");
        }
        String projectName = url.substring(url.lastIndexOf('/'),url.indexOf(".git"));
        Git git;
        try {
            git = Git.cloneRepository().setURI(url)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password))
                    .setBranch(branchName)
                    //.setCloneAllBranches(true)//获取所有分支
                    .setDirectory(new File("/temp" + File.separator + projectName + File.separator + branchName))//指定本地clone库
                    .call();
        }catch (Exception e){
            throw new BizException("git异常");
        }
        return git;
    }

    public void getRemoteBranchs(String url, String username, String password){
        try {
            Collection<Ref> refList;
            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
                UsernamePasswordCredentialsProvider pro = new UsernamePasswordCredentialsProvider(username, password);
                LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository().setRemote(url).setCredentialsProvider(pro);

                refList = lsRemoteCommand.call();

//                Repository repository = lsRemoteCommand.getRepository();
//                Git git = new Git(repository);
//                Iterable<RevCommit> revCommitList = git.log().call();
            } else {
                refList = Git.lsRemoteRepository().setRemote(url).call();
            }
            List<String> branchnameList = new ArrayList<>(4);
            for (Ref ref : refList) {
                String refName = ref.getName();
                if (refName.startsWith("refs/heads/")) {                       //需要进行筛选
                    String branchName = refName.replace("refs/heads/", "");
                    branchnameList.add(branchName);
                }

//
//                System.out.println("Branch: " + ref + " " + ref.getName() + " "
//						+ ref.getObjectId().getName());
//				ref.getObjectId();
//				RevCommit commit = revWalk.parseCommit( ref.getObjectId() );
            }

            System.out.println("共用分支" + branchnameList.size() + "个");
            branchnameList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }
}
