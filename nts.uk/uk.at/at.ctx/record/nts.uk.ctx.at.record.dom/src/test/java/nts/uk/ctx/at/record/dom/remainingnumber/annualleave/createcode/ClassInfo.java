package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.createcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * クラス情報
 * @author jinno
 */
@Getter
@Setter
public class ClassInfo {

	/**
	 * パッケージ
	 */
	private String pakage;

	/**
	 * importリスト
	 */
	private ArrayList<String> importList = new ArrayList<String>();

	/**
	 * クラスコメント
	 */
	private String classNameComment;

	/**
	 * クラス名
	 */
	private String className;

	/**
	 * メンバー変数リスト
	 */
	private ArrayList<MemberInfo> memberInfoList = new ArrayList<MemberInfo>();

	/**
	 * デバッグ用
	 * @param sb
	 */
	protected void setDebugString(StringBuilder sb) {

		sb.append("pakage = '");
		sb.append(pakage);
		sb.append(System.lineSeparator());

		sb.append("import ---------------------------------------------------");
		sb.append(System.lineSeparator());
		for( String str : importList) {
			sb.append(str);
			sb.append(System.lineSeparator());
		}

		sb.append("class ---------------------------------------------------");
		sb.append(System.lineSeparator());
		sb.append("classNameComment = '");
		sb.append(classNameComment);
		sb.append("'");
		sb.append(System.lineSeparator());

		sb.append("className = '");
		sb.append(className);
		sb.append("'");
		sb.append(System.lineSeparator());

		sb.append("member ---------------------------------------------------");
		sb.append(System.lineSeparator());
		for(MemberInfo memberInfo : memberInfoList) {
			memberInfo.setDebugString(sb);
			sb.append(System.lineSeparator());
		}
	}

	public String getDebugString() {
		StringBuilder sb = new StringBuilder();
		this.setDebugString(sb);
		return sb.toString();
	}


	/**
	 *
	 * @param filePath
	 */
	public void readFile(String filePath) {

		try {

			File file = new File(filePath);
			  FileReader filereader = new FileReader(file);
			  BufferedReader br = new BufferedReader(filereader);

			  String str;

			  // パッケージ  ----------------------------------------
			  str = br.readLine();
			  int beginIndex_package = -1;

			  while(str != null){

				  beginIndex_package = str.indexOf("package");
				  if ( beginIndex_package < 0 ) {
					  str = br.readLine();
					  continue;
				  }

				  int endIndex_package = str.indexOf(";");
				  String result = str.substring(beginIndex_package + 8, endIndex_package).trim();
				  this.setPakage(result);
				  break;

			  }

			  // コメント
			  int  beginIndex_comment = -1;
			  String comment = "";

			  // import  ----------------------------------------
			  str = br.readLine();
			  int beginIndex_import = -1;
			  int beginIndex_class = -1;
			  ArrayList<String> importList = new ArrayList<String>();

			  while(str != null){

				  // コメントチェック
				  beginIndex_comment = str.indexOf("/*");
				  if (0 <= beginIndex_comment ) { // コメント開始があるとき
					  CommentResult getCommentResult = getComment(br, str);
					  comment = getCommentResult.getComment();

					  // コメント前に文字列があるとき
					  if (3 < getCommentResult.getBeforeComment().length() ) {
						  str = getCommentResult.getBeforeComment();
					  } else {
						// コメント後に文字列があるときは一旦無視して先に進む
						  str = br.readLine();
					  }
					  beginIndex_comment = -1;
				  }

				  if ( str==null ) {
					  break;
				  }

				  beginIndex_import = str.indexOf("import ");
				  if ( 0 <= beginIndex_import ) {
					  // int endIndex_import = str.indexOf(";");
					  String result = str.substring(beginIndex_import+ 6).trim();
					  importList.add(result);
				  }

				  beginIndex_class = str.indexOf("class ");
				  if ( 0 <= beginIndex_class ) {
					  this.setImportList(importList);
					  break;
				  }
				  str = br.readLine();
			  }

			  // クラス  ----------------------------------------
			  while(str != null){

				  // コメントチェック
				  beginIndex_comment = str.indexOf("/*");
				  if (0 <= beginIndex_comment ) { // コメント開始があるとき
					  CommentResult commentResult = getComment(br, str);
					  comment = commentResult.getComment();

					  // コメント前に文字列があるとき
					  if (3 < commentResult.getBeforeComment().length() ) {
						  str = commentResult.getBeforeComment();
					  } else {
						// コメント後に文字列があるときは一旦無視して先に進む
						  str = br.readLine();
					  }
					  beginIndex_comment = -1;
				  }

				  beginIndex_class = str.indexOf("class ");
				  // class があるとき
				  if (0 <= beginIndex_class) {
					 String className = str.substring(beginIndex_class).replace("class ", "").replace("{", "").trim();
					 this.setClassName(className);

					 comment = comment.replace("/*", "").replace("*", "").trim();
					 this.setClassNameComment(comment);
					 comment = "";
					 str = br.readLine();
					 break;
				  }

				  // class があるまでループを抜けない
				  str = br.readLine();
			  }

			  // メンバー変数 ----------------------------------------
			  memberInfoList = new ArrayList<MemberInfo>();
			  comment = "";
			  while(str != null){

				  // コメントチェック１
				  beginIndex_comment = str.indexOf("/*");
				  if (0 <= beginIndex_comment ) { // コメント開始があるとき
					  CommentResult commentResult = getComment(br, str);
					  comment = commentResult.getComment();

					  // コメント前に文字列があるとき
					  if (3 < commentResult.getBeforeComment().length() ) {
						  str = commentResult.getBeforeComment();
					  } else {
						// コメント後に文字列があるときは一旦無視して先に進む
						  str = br.readLine();
					  }
					  beginIndex_comment = -1;
				  }
				// コメントチェック２
				  beginIndex_comment = str.indexOf("//");
				  if (0 <= beginIndex_comment ) { // コメント開始があるとき
					  comment = str.substring(beginIndex_comment).replace("//", "").trim();

					  // コメント前に文字列があるとき
					  if (3 < beginIndex_comment ) {
						  String beforeComment = str.substring(0, beginIndex_comment-1).trim();
						  str = beforeComment;
					  } else {
						// コメント後に文字列があるときは一旦無視して先に進む
						  str = br.readLine();
					  }
					  beginIndex_comment = -1;
				  }

				  if ( str != null ) {
					  if ( str.contains("private ") || str.contains("protected ") ||  str.contains("public ")  ) {

						  if ( !str.contains("{ ") && !str.contains("(") ) { // 関数は除く

							  MemberInfo memberInfo = new MemberInfo();

							  String memberString = str;
							  if ( str.contains("private ") ) {
								  memberInfo.setPrivate_(true);
								  memberString = memberString.replace("private ", "");
							  }
							  if ( str.contains("protected ") ) {
								  memberInfo.setProtected_(true);
								  memberString = memberString.replace("protected ", "");
							  }
							  if ( str.contains("public ") ) {
								  memberInfo.setPublic_(true);
								  memberString = memberString.replace("public ", "");
							  }
							  if ( str.contains("final ") ) {
								  memberInfo.setFinal_(true);
								  memberString = memberString.replace("final ", "");
							  }
							  if ( str.contains("static ") ) {
								  memberInfo.setStatic_(true);
								  memberString = memberString.replace("static ", "");
							  }

							  // 型
							  int beginIndexSpace = memberString.indexOf(" ");
							  if ( 0 < beginIndexSpace) {
								  String memberType = memberString.substring(0, beginIndexSpace).trim();
								  if ( memberType.contains("Optional<")) {
									  memberInfo.setOptional(true);








								  }


								  memberInfo.setMemberType(memberType);
								  memberString = memberString.substring(beginIndexSpace).trim();

							  }

							  // 名前
							  int beginIndexEqual = memberString.indexOf("=");
							  int beginIndexEnd = memberString.indexOf(";");
							  if ( 0 < beginIndexEqual) {
								  String memberName = memberString.substring(0, beginIndexEqual).trim();
								  memberInfo.setMemberName(memberName);
							  } else if ( 0 < beginIndexEnd) {
								  String memberName = memberString.substring(0, beginIndexEnd).replace(";", "").trim();
								  memberInfo.setMemberName(memberName);
							  }

							  // コメント
							  comment = comment.replace("/*", "").replace("*", "").trim();
							  memberInfo.setMemberNameComment(comment);
							  comment = "";

							  memberInfoList.add(memberInfo);
						  }
					  }
				  }

				  //  最後まで
				  str = br.readLine();
			  }

			  this.setMemberInfoList(memberInfoList);

			  br.close();

			} catch(FileNotFoundException e) {
				System.out.println(e);
			} catch(IOException e) {
				System.out.println(e);
			}

		}

		/**
		 * コメント解析
		 *
		 * @param br
		 * @param str
		 * @return
		 * @throws IOException
		 */
		private CommentResult getComment(BufferedReader br, String str) throws IOException {

			CommentResult commentResult = new CommentResult();

			// コメント
			  StringBuilder  sb_comment = new StringBuilder();
			  String comment = "";
			  int  beginIndex_comment = -1;
			  int  endIndex_comment = -1;
			  boolean isCommentMultiLine = false;

			  while(str != null){

				  // 新規コメントチェック
				  if (beginIndex_comment < 0) {
					  beginIndex_comment = str.indexOf("/*");
					  if ( 0 < beginIndex_comment ) {
						  commentResult.setBeforeComment(str.substring(0,beginIndex_comment ));
					  }
				  }

				 // 新規コメントか複数行コメントのとき
				  if (0 <= beginIndex_comment || isCommentMultiLine) {

					  endIndex_comment = str.indexOf("*/");
					  if ( 0 <= endIndex_comment ) { // コメント終了があるとき

						  // コメント取得
						  if ( isCommentMultiLine ) {
							  sb_comment.append( str.substring(0, endIndex_comment));
							  comment = sb_comment.toString();
						  } else {
							  comment = str.substring(beginIndex_comment, endIndex_comment);
						  }
						  commentResult.setComment(comment);
						  commentResult.setAfterComment(str.substring(endIndex_comment));

						 return commentResult;

					  } else { // コメント終了がないとき

						  // コメント追加
						  if ( isCommentMultiLine) {
							  sb_comment.append(str);
						  } else {
							  sb_comment.append(str.substring(beginIndex_comment));
						  }
						  isCommentMultiLine = true;

						  str = br.readLine(); // コメントの終了がないときは、次の行へ
						  continue;
					  }
				  }
				  str = br.readLine();
			  }
			  return commentResult;
		}

}


