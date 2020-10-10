package com.tiankong44.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @ClassName MarkdownUtil
 * @Description TODO
 * @Author 12481
 * @Date 18:16
 * @Version 1.0
 **/
public class MarkdownUtil {

    public static String markdownToHtml(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);

    }

    static class CustomAttributeProvider implements AttributeProvider {

        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "layui-table");
            }
            if (node instanceof Image) {
                attributes.put("style", "width:500px;height:350px;position:relative;");
                attributes.put("class", "blog-img");
            }

        }
    }

    public static void main(String[] args) {
        String str = "### 软件测试与用例编写\n" +
                "\n" +
                "#### 软件测试概述\n" +
                "\n" +
                "##### 软件测试的对象\n" +
                "\n" +
                "1. 软件测试不等于程序测试\n" +
                "2. 软件测试贯串于软件定义和开发的整个期间o\n" +
                "3. 需求规格说明、概要设计规格说明、详细设计规格说明、程序、数据等都是软件测试的对象○\n" +
                "\n" +
                "##### 软件测试的目的\n" +
                "\n" +
                "1. 证明程序的正确性――除非仅处理有限种情况\n" +
                "2. 检查系统是否满足需求―一期望目标\n" +
                "3. 发现程序错误―一直接目标\n" +
                "4. 测试是以评价一个程序或者系统属性为目标的一种活动，测试是对软件质量的度量与评估，以验证软件的质量满足用户的需求，为用户选择与接受软件提供有力的依据\n" +
                "\n" +
                "##### 软件测试的原则\n" +
                "\n" +
                "1. 所有的软件测试都应追溯到用户需求\n" +
                "2. 避免测试自己的程序。\n" +
                "3. 执行测试计划，排除随意性。\n" +
                "4. 增量测试，由小到大。\n" +
                "5. 周密的测试用例(输入条件(合理、不合理)、预期输出结果)\n" +
                "6. 充分注意群集现象。\n" +
                "7. 问题的互相确认。\n" +
                "8. 完全测试是不可能的，测试需要终止回归测试。出错统计和分析。\n" +
                "9. 妥善保存一切测试过程文档\n" +
                "\n" +
                "#### 软件测试计划概述\n" +
                "\n" +
                "**测试计划的定义**\n" +
                "\n" +
                "- 一个叙述了预定的测试活动的范围、途径、资源及进度安排的文档。它确认了测试项、被侧特征、测试任务、人员安排以及任何偶发计划的风险。\n" +
                "- 《ANSI/IEEE软件测试文档标准829-1983》\n" +
                "\n" +
                "**测试计划的作用**\n" +
                "\n" +
                "- 为测试过程提供指导\n" +
                "  1. 测试目标\n" +
                "  2. 测试内容\n" +
                "  3. 测试方法\n" +
                "  4. 测试时间周期\n" +
                "- 改善测试任务与测试过程的关系\n" +
                "- 提高测试的组织、规划和管理能力\n" +
                "\n" +
                "**测试计划的内容**\n" +
                "\n" +
                "- 测试项目简介\n" +
                "- 测试项\n" +
                "- 需要测试的特征\n" +
                "- 不需要测试的特征\n" +
                "- 测试的方法(测试人员丶测试工具、测试流程)\n" +
                "- 测试开始条件和结束条件\n" +
                "- 测试提交的结果与格式\n" +
                "- 测试环境(软件、硬件、网络)\n" +
                "- 测试者的任务、联系方式与培训\n" +
                "- 测试进度与跟踪方式\n" +
                "- 测试风险与解决方式\n" +
                "- 本测试计划的审批与变更方式\n" +
                "\n" +
                "#### 软件测试过程\n" +
                "\n" +
                "```mermaid\n" +
                "   graph TD\n" +
                "   测试计划 -->测试设计-->测试执行-->测试记录-->分析-->缺陷跟踪\n" +
                "   分析-->完毕-->测试总结\n" +
                "```\n" +
                "![](http://images.tiankong44.vip/1585308331112)\n" +
                "\n" +
                "#### 测试用例设计方法\n" +
                "\n" +
                "##### 设计测试用例的基本准则\n" +
                "\n" +
                "- 测试用例的代表性\n" +
                "\n" +
                "  能够代表并覆盖各种合理的和不合理的、合法的和非法的、边界的和越\n" +
                "  界的以及极限的输入数据、操作和环境设置等。\n" +
                "\n" +
                "- 测试结果的可判定性\n" +
                "\n" +
                "  即测试执行结果的正确性是可判定的，每一个测试用例都应有相应的期\n" +
                "  望结果。\n" +
                "\n" +
                "- 测试结果的可再现性\n" +
                "\n" +
                "\u200B        即对同样的测试用例，系统的执行结果应当是相同的\n" +
                "\n" +
                "##### 测试用例的基本要素\n" +
                "\n" +
                "- 测试项：当前测试用例所属测试大类、被测需求、被测模块、被测单元等如果是工程项目，则取需求矩阵\n" +
                "- 测试目的：描述设计此测试用例的目的和用途是什么\n" +
                "- 测试要点：需要用概括的语言描述该用例的出发点和关注点，测试过程中需要重点关注的内容\n" +
                "- 前提条件：执行此测试之前需要做的准备或者执行此条测试用例之前的前提步骤。需要提前准备好的数据，包括号卡资源、工号、话单、账单等\n" +
                "- 测试步骤：执行当前测试用例需要经过的操作步骤，需要明确的给出每一个步骤的描述，测试用例执行人员可以根据该操作步骤完成测试用例执行\n" +
                "- 预期结果：当前测试用例的预期输出结果，包括返回值的内容、界面的相应结果、输出结果的规则等等\n" +
                "\n" +
                "##### 良好测试用例的特征\n" +
                "\n" +
                "1. 可以最大程度地找出软件隐藏的缺陷\n" +
                "2. 可以最高效率的找出软件缺陷\n" +
                "3. 可以最大程度地满足测试覆盖要求\n" +
                "4. 既不过分复杂、也不能过分简单\n" +
                "5. 使软件缺陷的表现可以清楚的判定\n" +
                "   - 测试用例包含期望的正确的结果\n" +
                "   - 待查的输出结果或文件必须尽量简单明了\n" +
                "6. 不包含重复的测试用例\n" +
                "7. 测试用例内容清晰、格式一致、分类组织\n" +
                "\n" +
                "#### 测试用例设计方法\n" +
                "\n" +
                "- 等价类划分法\n" +
                "- 边界值分析法\n" +
                "- 错误推测法\n" +
                "- 因果图法测试用例设计\n" +
                "- 场景设计法\n" +
                "- 探索性测试\n" +
                "\n" +
                "###### 等价类划分思想\n" +
                "\n" +
                "1. 等价类划分设计方法是把所有可能的成若干部分(子集)然后从每一个子集中选取少量具有代表性的数据作为测试用例。\n" +
                "\n" +
                "2. 等价类是指某个输入域的子集合。在该子集合中，各个输入数据对于\n" +
                "\n" +
                "   揭露程序中的错误都是等效的。\n" +
                "\n" +
                "3. 并合理地假定：测试某等价类的代表值就等效于对这一类其他值的测\n" +
                "   试。\n" +
                "\n" +
                "\n" +
                "###### 边界值分析法\n" +
                "\n" +
                "- 程序的很多错误发生在输入或输出范围的边界上，因此针\n" +
                "  对各种边界情况设置测试用例，可以发现不少程序缺陷\n" +
                "- 设计方法：\n" +
                "  1. 确定边界情况(输入或输出等价类的边界)\n" +
                "  2. 选取正好等于、刚刚大于或刚刚小于边界值作为测试数据\n" +
                "\n" +
                "###### 错误推测法测试用例设计\n" +
                "\n" +
                "- 基于经验和直觉推测程序中所有可能存在的各种错误,从而有针对性地设计测试用例。\n" +
                "- 发现程序经常出现的错误的方法:\n" +
                "  1. 单元测试中发现的模块错误；\n" +
                "  2. 产品的以前版本曾经发现的错误；\n" +
                "  3. 输入数据为0或字符为空；\n" +
                "  4. 当软件要求输入时(比如在文本框中)，本没有输入任何内容， 单单按了Enter这种情况在产品说明书中常常忽视，程使用中却时有发生。程序员总会习惯性看起来合法的或非法的信息，要不就会不是没有输入正确的信息，而是根键；序员也可能经常遗忘，但是在实际的认为用户要么输入信息，不管是选择Cancel键放弃输入，\n" +
                "\n" +
                "###### 因果图法测试用例设计\n" +
                "\n" +
                "- 多种输入条件的组合，产生多种结果测试用例\n" +
                "- 设计方法：\n" +
                "  1. 分析软件规格说明文档描述的哪些是原因输出条件))，给每个原因赋予一个标示符。\n" +
                "  2. 找出原因与结果，原因与原因之间的对应关系，划出因果图\n" +
                "  3. 在因果图上标上哪些不可能发生的因果关系，表明约束或限制条件\n" +
                "  4. 根据因果图，创建判定表，将复杂的逻辑关系和多种条件组合很具体明确的表示出来\n" +
                "  5. 把判定表的每一行作为依据设计测试用例。\n" +
                "\n" +
                "###### 场景设计法\n" +
                "\n" +
                "现在的软件大部分是场景法一般包含基本由事件触发来控制流程的，事件\n" +
                "测试用例设计过程中，通过描述试人员的设计思维，同时对测试用例的理解和执行也有很大的帮助\n" +
                "\n" +
                "场景法一般包含基本流和备选流，从一个流程开始，通过描述\n" +
                "经过的路径来确定的过程，经过遍历所有的基本流和备选流来完成整\n" +
                "个场景。\n" +
                "\n" +
                "**场景法的基本设计步骤**\n" +
                "\n" +
                "1. 根据说明，描述出程序的基本流及各项备选流\n" +
                "2. 根据基本流和各项备选流生成不同的场景\n" +
                "3. 对每一个场景生成相应的测试用例\n" +
                "4. 对生成的所有测试用例重新复审，去掉多余的测试用例，测试用例确定后，对每一个测试用例确定测试数据值\n" +
                "\n" +
                "###### 探索性测试\n" +
                "\n" +
                "探索性测试方法是测试经验丰富的测试人员喜欢使用的一种测\n" +
                "试用例设计方法。\n" +
                "\n" +
                "探索性测试方法通过基于经验和直觉推测程序中可能发生的各种错误，有针对性地设计测试用例。因为测试本质上并不是一门非常严谨的学科，测试人员的经验和直觉能对这种不严谨性做出很好的补充\n" +
                "\n" +
                "#### 压力测试用例案例\n" +
                "\n" +
                "##### 设计目的\n" +
                "\n" +
                "- 验证程序在承受某种负载或压力\n" +
                "  下是否能够正常运行\n" +
                "- 找出程序安全运行的临界值\n" +
                "\n" +
                "##### 适用情形：\n" +
                "\n" +
                "- 服务器/客户机局域网\n" +
                "- 服务器/浏览器互联网\n" +
                "\n" +
                "##### 设计方法：\n" +
                "\n" +
                "- 设计出不同等级的压力条件\n" +
                "\n" +
                "##### 压力/负载分类与代号：\n" +
                "\n" +
                "- CPU使用量\n" +
                "- 磁盘空间\n" +
                "- 物理内存\n" +
                "- 虚拟内存使用量\n" +
                "- 登录用户的数量\n" +
                "- I/0接口\n" +
                "\n" +
                "#### 缺陷处理流程\n" +
                "\n" +
                "##### 什么是缺陷?\n" +
                "\n" +
                "缺陷：最终产品同用户的期望不一致\n" +
                "\n" +
                "##### 缺陷的分类\n" +
                "\n" +
                "**错误**\n" +
                "**遗漏**\n" +
                "\n" +
                "##### 超出需求的部分\n" +
                "\n" +
                "缺陷(未触发)VS.错误(应首先解决)\n" +
                "\n" +
                "**流程图**\n" +
                "\n" +
                "```mermaid\n" +
                "   \n" +
                "   graph TD\n" +
                "   测试人员发现bug -->开发人员解决-->测试人员复制-->是否通过-->结束\n" +
                "   是否通过-->开发人员解决\n" +
                "    \n" +
                "   \n" +
                "```\n" +
                "![](http://images.tiankong44.vip/1585308335563)\n";
        System.out.println(markdownToHtml(str));
    }
}
