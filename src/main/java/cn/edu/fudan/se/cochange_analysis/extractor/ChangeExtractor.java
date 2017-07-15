package cn.edu.fudan.se.cochange_analysis.extractor;

import java.io.File;
import java.util.List;
import java.util.UUID;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommitParentKey;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitChangeFileDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitParentDAO;

public class ChangeExtractor {
	private GitRepository repository;

	public ChangeExtractor() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		ChangeExtractor changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();
	}

	public void extracChange() {
		int repositoryId = repository.getRepositoryId();
		// System.out.println(repositoryId);
		GitExtractor gitExtractor = new GitExtractor(repository);
		// create temp directory to store files to be extracted
		String userDirPath = System.getProperty("user.dir");
		String tempDirPath = userDirPath + "/" + UUID.randomUUID().toString();
		File tempDir = new File(tempDirPath);
		tempDir.mkdirs();

		List<GitChangeFile> changeFiles = GitChangeFileDAO.selectFilteredByRepositoryId(repositoryId);

		for (GitChangeFile changeFile : changeFiles) {
			// not MODIFY
			if (!changeFile.getChangeType().equals("MODIFY"))
				continue;

			String commitId = changeFile.getCommitId();
			List<GitCommitParentKey> commitParents = GitCommitParentDAO.selectByRepositoryIdAndCommitId(repositoryId,
					commitId);
			if (commitParents.size() != 1)
				continue;

			String parentCommitId = commitParents.get(0).getParentCommitId();
			String filePath = changeFile.getFileName();

			// System.out.println(repositoryId + "," + commitId + "," +
			// filePath);

			byte[] content1 = gitExtractor.getFileContentByCommitId(parentCommitId, filePath);
			byte[] content2 = gitExtractor.getFileContentByCommitId(commitId, filePath);
			String randomString = UUID.randomUUID().toString();
			// create temp files before and after the commit
			File left = FileUtils.writeBytesToFile(content1, tempDirPath, randomString + ".v1");
			File right = FileUtils.writeBytesToFile(content2, tempDirPath, randomString + ".v2");

			FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
			try {
				distiller.extractClassifiedSourceCodeChanges(left, right);
			} catch (Exception e) {
				System.err.println("Warning: error while change distilling. " + e.getMessage());
			}

			// delete temp files
			left.delete();
			right.delete();

			List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
			if (!changes.isEmpty())
				ChangeOperationDAO.insertChanges(changes, repositoryId, commitId, filePath);
		}
		// System.out.println();
		// delete temp directory
		tempDir.delete();
		// System.out.println();

	}

	public ChangeExtractor(GitRepository gitRepository) {
		this.repository = gitRepository;
	}

	public GitRepository getGitRepository() {
		return repository;
	}

	public void setGitRepository(GitRepository gitRepository) {
		this.repository = gitRepository;
	}
}
