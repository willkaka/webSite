package com.hyw.webSite.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import com.hyw.webSite.dto.GitCommitInfoDto;
import com.hyw.webSite.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.ReflogEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Git仓库查询功能
 * @author huangyuanwei
 */
@Service
public class JGitService {

//	@Test
//	public void test(){
//		String gitLocalPath = "D:\\Java\\DaShuSource\\caes_release_001";
//		//String gitLocalPath = "http://gitlab.dashuf.com/ds2/caes/tree/20201215_develop_5457_%E6%9A%82%E5%81%9C%E6%89%A3%E6%AC%BE%E5%AF%BC%E5%85%A5%E5%8A%9F%E8%83%BD";
//		String sCommitUser = "";
//		LocalDateTime sBegDate = LocalDateTime.of(2020,11,06,0,0);
//		LocalDateTime sEndDate = LocalDateTime.now();
//
//		Map<String,List<GitCommitInfoDto>> fileList = new HashMap<>();
//
//		List<GitCommitInfoDto> gitCommitInfoDtos = getCommitList(gitLocalPath,sCommitUser,sBegDate,sEndDate);
//		//List<GitCommitInfoDto> gitCommitInfoDtos = getCommitList(gitLocalPath,sCommitUser,null,null);
//		for(GitCommitInfoDto gitCommitInfoDto:gitCommitInfoDtos){
//			for(DiffEntry commitInfo:gitCommitInfoDto.getDiffEntryList()){
//				String sPath = commitInfo.getOldPath();
//				List<GitCommitInfoDto> gitCommitInfoDtoList = fileList.get(sPath);
//				if(!CollectionUtils.isEmpty(gitCommitInfoDtoList)){
//					gitCommitInfoDtoList.add(gitCommitInfoDto);
//				}else {
//					List<GitCommitInfoDto> gitCommitInfoDtoList2 = new ArrayList<>();
//					gitCommitInfoDtoList2.add(gitCommitInfoDto);
//					fileList.put(sPath, gitCommitInfoDtoList2);
//				}
//			}
//		}
//		//System.out.println(fileList);
//
//		fileList.forEach((fileName,changeList) ->{
//			System.out.println(fileName);
//			changeList.forEach(commitInfo -> {
//				System.out.println("  "+commitInfo.getCommitDateTime()+"  "+commitInfo.getCommitterIdent().getName()+"  "+commitInfo.getShortMessage());
//			});
//		});
//	}

	/**
	 * 取程序提交信息
	 * @param gitLocalPath git路径
	 * @return Map<String,List<GitCommitInfoDto>>
	 */
	public Map<String,List<GitCommitInfoDto>> getFileCommintInfo(String gitLocalPath,String user,LocalDateTime begTime,LocalDateTime endTime){
		//String gitLocalPath = "D:\\Java\\DaShuSource\\caes_release_001";
		//String gitLocalPath = "http://gitlab.dashuf.com/ds2/caes/tree/20201215_develop_5457_%E6%9A%82%E5%81%9C%E6%89%A3%E6%AC%BE%E5%AF%BC%E5%85%A5%E5%8A%9F%E8%83%BD";
//		String user = "";
//		LocalDateTime begTime = LocalDateTime.of(2020,11,06,0,0);
//		LocalDateTime endTime = LocalDateTime.now();

		Map<String,List<GitCommitInfoDto>> fileList = new HashMap<>();

		List<GitCommitInfoDto> gitCommitInfoDtos = getCommitList(gitLocalPath,user,begTime,endTime);
		//List<GitCommitInfoDto> gitCommitInfoDtos = getCommitList(gitLocalPath,user,null,null);
		for(GitCommitInfoDto gitCommitInfoDto:gitCommitInfoDtos){
			for(DiffEntry commitInfo:gitCommitInfoDto.getDiffEntryList()){
				String sPath = commitInfo.getOldPath();
				List<GitCommitInfoDto> gitCommitInfoDtoList = fileList.get(sPath);
				if(!CollectionUtils.isEmpty(gitCommitInfoDtoList)){
					gitCommitInfoDtoList.add(gitCommitInfoDto);
				}else {
					List<GitCommitInfoDto> gitCommitInfoDtoList2 = new ArrayList<>();
					gitCommitInfoDtoList2.add(gitCommitInfoDto);
					fileList.put(sPath, gitCommitInfoDtoList2);
				}
			}
		}
		//System.out.println(fileList);

//		fileList.forEach((fileName,changeList) ->{
//			System.out.println(fileName);
//			changeList.forEach(commitInfo -> {
//				System.out.println("  "+commitInfo.getCommitDateTime()+"  "+commitInfo.getCommitterIdent().getName()+"  "+commitInfo.getShortMessage());
//			});
//		});
		return fileList;
	}

	/**
	 * 取提交历史记录
	 * @param gitLocalPath git本地文件路径
	 * @param commitUser 提交用户名
	 * @param begDateTime 查询开始日期
	 * @param endDateTime 查询结束日期
	 * @return ArrayList<HashMap<String,Object>>
	 */
	public List<GitCommitInfoDto> getCommitList(String gitLocalPath, String commitUser,
												LocalDateTime begDateTime, LocalDateTime endDateTime){
        List<GitCommitInfoDto> commitList = new ArrayList<>();
		File gitDir = new File(gitLocalPath);

		try {
			Git git = Git.open(gitDir);

			int c = 0;
			//ListMode设置为ALL或REMOTE.默认的ListMode(null)仅返回本地分支
//			List<Ref> call = git.branchList().setListMode( ListBranchCommand.ListMode.REMOTE ).call();
//			for (Ref ref : call) {
//				System.out.println("Branch: " + ref + " " + ref.getName() + " "
//						+ ref.getObjectId().getName());
//				c++;
//				ref.getObjectId();
//				RevCommit commit = revWalk.parseCommit( ref.getObjectId() );
//			}
//			System.out.println("Number of branches: " + c);
//			System.out.println(git.getRepository());

//			Collection<ReflogEntry> reflogs = git.reflog().call();
//			reflogs.forEach(reflogEntry -> {
//				System.out.println(reflogEntry.getWho().getWhen()+ " "+reflogEntry.getWho().getName()
//						+ reflogEntry.getComment());
//			});

			Iterable<RevCommit> gitlog= git.log().call();
			for (RevCommit revCommit : gitlog) {
				LocalDateTime commitTime = TimeUtil.intTime2LocalDateTime(revCommit.getCommitTime());
				String sCommitByUser = revCommit.getAuthorIdent().getName();
				//提交用户筛选
				if(StringUtils.isNotBlank(commitUser) &&
				   !"ALL".equals(commitUser.toUpperCase()) &&
				   !commitUser.equals(sCommitByUser)) continue;
				//提交时间筛选
				if(null != begDateTime && commitTime.compareTo(begDateTime) < 0) continue;
				if(null != endDateTime && commitTime.compareTo(endDateTime) > 0) continue;
				if(commitUser.equals(sCommitByUser) &&
						revCommit.getShortMessage().length() > 5 &&
						revCommit.getShortMessage().substring(0, 5).equals("Merge")) continue;

				//满足条件返回数据
				GitCommitInfoDto gitCommitInfoDto = new GitCommitInfoDto();
				gitCommitInfoDto.setCommitId(revCommit.getName());
				gitCommitInfoDto.setAuthorIdent(revCommit.getAuthorIdent());
				gitCommitInfoDto.setCommitterIdent(revCommit.getCommitterIdent());
				gitCommitInfoDto.setCommitDateTime(TimeUtil.intTime2LocalDateTime(revCommit.getCommitTime()));
				gitCommitInfoDto.setEncoding(revCommit.getEncoding());
				gitCommitInfoDto.setEncodingName(revCommit.getEncodingName());
				gitCommitInfoDto.setFooterLines(revCommit.getFooterLines());
				//gitCommitInfoDto.setFooterKeyList(revCommit.getFooterLines());
				gitCommitInfoDto.setFullMessage(revCommit.getFullMessage());
				gitCommitInfoDto.setParent(revCommit.getParent(0));
				gitCommitInfoDto.setParentCount(revCommit.getParentCount());
				gitCommitInfoDto.setParents(revCommit.getParents());
				gitCommitInfoDto.setRawBuffer(revCommit.getRawBuffer());
				gitCommitInfoDto.setRawGpgSignature(revCommit.getRawGpgSignature());
				gitCommitInfoDto.setShortMessage(revCommit.getShortMessage());
				gitCommitInfoDto.setTree(revCommit.getTree());
				gitCommitInfoDto.setType(revCommit.getType());
				gitCommitInfoDto.setDiffEntryList(getDifEntLst(git,gitCommitInfoDto.getCommitId()));

				commitList.add(gitCommitInfoDto);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
        return commitList;
    }  
	
    /**
     * 查询本次提交的日志
     * @param git git仓库
     * @param commitId  版本号
     * @return List<DiffEntry>
     */
    public static List<DiffEntry> getDifEntLst(Git git, String commitId) throws Exception {
        Repository repository = git.getRepository();
        Iterator<RevCommit> iter = git.log().add(repository.resolve(commitId)).call().iterator();
        RevCommit commit = iter.next();
        TreeWalk tw = new TreeWalk(repository);
        int x = tw.addTree(commit.getTree());

        commit = iter.next();
        if (null == commit) return null;
        tw.addTree(commit.getTree());
        tw.setRecursive(true);
        RenameDetector rd = new RenameDetector(repository);
        rd.addAll(DiffEntry.scan(tw));

        return rd.compute();
    }

    public Git getGit(String projectName, String branchName) throws GitAPIException {
		// 如果目录存在就先更新后open
		// "D:\\Java\\DaShuSource\\caes_develop_01";
		return Git.cloneRepository().setURI("http://gitlab.dashuf.com/ds2/caes.git")
			.setCredentialsProvider(new UsernamePasswordCredentialsProvider("huangyuanwei", "Dashuf0930"))
			.setBranch(branchName)
			//.setCloneAllBranches(true)//获取所有分支
			.setDirectory(new File("D:/repo" + File.separator + projectName))//指定本地clone库
			.call();
	}
}
