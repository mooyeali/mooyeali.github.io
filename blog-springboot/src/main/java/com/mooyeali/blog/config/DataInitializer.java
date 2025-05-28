package com.mooyeali.blog.config;

import com.mooyeali.blog.model.BlogPost;
import com.mooyeali.blog.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (blogPostRepository.count() == 0) {
            initializeBlogPosts();
        }
    }
    
    private void initializeBlogPosts() {
        // 创建示例博客文章
        BlogPost post1 = new BlogPost(
            "记一次糟糕的开发经历(文本匹配器)--swing框架使用",
            "利用Java Swing开发文本匹配器的全过程，旨在通过输入文本和选择文件来查找信息。主要内容包括使用Swing构建GUI、Apache POI处理Office文档、实现文件选择与读取功能，并为组件添加事件监听。最终，项目被打包成可执行文件，方便部署和使用。尽管过程充满挑战，但成功实现了目标功能并分享了源码。",
            "项目背景\n\n在日常开发工作中，经常需要处理各种文档和文本文件，查找特定的信息。为了提高工作效率，我决定开发一个文本匹配器工具。\n\n技术选型\n\n考虑到需要快速开发一个桌面应用程序，我选择了Java Swing作为GUI框架，Apache POI用于处理Office文档。\n\n主要功能\n\n- 支持多种文件格式：txt、doc、docx、xls、xlsx等\n- 文本搜索和匹配\n- 结果高亮显示\n- 批量文件处理\n\n开发过程中的挑战\n\n1. 文件编码问题：不同的文本文件可能使用不同的编码格式，需要自动检测和处理。\n2. 内存管理：处理大文件时容易出现内存溢出，需要优化读取策略。\n3. UI响应性：文件处理过程中UI容易卡顿，需要使用多线程处理。\n\n解决方案\n\n通过使用SwingWorker类来处理后台任务，避免UI线程阻塞。同时实现了进度条显示，提升用户体验。\n\n总结\n\n虽然开发过程中遇到了很多问题，但最终还是成功完成了这个项目。这次经历让我对Java Swing有了更深入的理解，也学会了如何处理复杂的文件操作。"
        );
        
        BlogPost post2 = new BlogPost(
            "Java IO流之IO流基础",
            "深入讲解Java IO流的基础知识，包括字节流、字符流的概念和使用方法，以及常见的IO操作模式和最佳实践。",
            "什么是IO流\n\nIO流是Java中用于处理输入输出操作的核心机制。通过流的概念，我们可以统一处理各种数据源的读写操作。\n\n流的分类\n\n按数据类型分类：\n- 字节流：以字节为单位进行数据传输，适合处理二进制数据\n- 字符流：以字符为单位进行数据传输，适合处理文本数据\n\n按数据流向分类：\n- 输入流：从数据源读取数据到程序\n- 输出流：从程序写入数据到目标\n\n常用的IO类\n\n字节流：\n- InputStream/OutputStream：抽象基类\n- FileInputStream/FileOutputStream：文件操作\n- BufferedInputStream/BufferedOutputStream：缓冲流\n\n字符流：\n- Reader/Writer：抽象基类\n- FileReader/FileWriter：文件操作\n- BufferedReader/BufferedWriter：缓冲流\n\n最佳实践\n\n1. 使用try-with-resources语句自动关闭资源\n2. 优先使用缓冲流提高性能\n3. 根据数据类型选择合适的流\n4. 注意字符编码问题"
        );
        
        BlogPost post3 = new BlogPost(
            "NIO系列之NIO和缓冲区(一)",
            "介绍Java NIO的基本概念和核心组件，重点讲解缓冲区(Buffer)的工作原理和使用方法。",
            "什么是NIO\n\nNIO（New IO）是Java 1.4引入的新的IO API，提供了与传统IO不同的工作方式。NIO是面向缓冲区的、非阻塞的IO操作。\n\nNIO的核心组件\n\n- Channel（通道）：类似于传统IO中的流，但是双向的\n- Buffer（缓冲区）：数据的容器，所有数据都通过Buffer处理\n- Selector（选择器）：用于监控多个Channel的状态\n\nBuffer详解\n\nBuffer是NIO中的核心概念，它是一个对象，包含一些要写入或读出的数据。\n\nBuffer的重要属性：\n- capacity：缓冲区的容量\n- position：当前位置\n- limit：限制位置\n- mark：标记位置\n\nBuffer的常用方法：\n- flip()：切换到读模式\n- clear()：清空缓冲区\n- rewind()：重置position为0\n- mark()和reset()：标记和恢复位置\n\nBuffer的工作流程\n\n1. 写入数据到Buffer\n2. 调用flip()方法\n3. 从Buffer中读取数据\n4. 调用clear()或compact()方法\n\n示例代码\n\nByteBuffer buffer = ByteBuffer.allocate(1024);\n// 写入数据\nbuffer.put(\"Hello NIO\".getBytes());\n// 切换到读模式\nbuffer.flip();\n// 读取数据\nbyte[] data = new byte[buffer.remaining()];\nbuffer.get(data);"
        );
        
        BlogPost post4 = new BlogPost(
            "为什么不推荐使用 kill -9",
            "详细解释为什么在Linux系统中不推荐使用kill -9命令强制终止进程，以及更好的替代方案。",
            "kill命令简介\n\nkill命令用于向进程发送信号，最常用的是终止进程。不同的信号有不同的作用。\n\n常见的信号类型\n\n- SIGTERM (15)：请求进程正常终止\n- SIGKILL (9)：强制终止进程\n- SIGINT (2)：中断信号，通常由Ctrl+C触发\n- SIGHUP (1)：挂起信号\n\nkill -9的问题\n\n1. 无法优雅关闭\nkill -9会立即终止进程，不给进程任何清理的机会，可能导致：\n- 数据丢失\n- 临时文件未清理\n- 数据库连接未正常关闭\n- 锁资源未释放\n\n2. 可能产生僵尸进程\n如果父进程没有正确处理子进程的退出，可能产生僵尸进程。\n\n3. 影响系统稳定性\n强制终止关键进程可能影响系统稳定性。\n\n推荐的做法\n\n1. 首先尝试SIGTERM\nkill 进程ID\n或者\nkill -15 进程ID\n\n2. 等待一段时间\n给进程一些时间来完成清理工作。\n\n3. 如果必要，再使用SIGKILL\nkill -9 进程ID\n\n最佳实践\n\n1. 优先使用应用程序提供的停止命令\n2. 使用SIGTERM信号\n3. 等待合理的时间\n4. 最后才考虑SIGKILL"
        );
        
        BlogPost post5 = new BlogPost(
            "1417. 重新格式化字符串[LeetCode 系列]",
            "LeetCode第1417题的详细解题思路和代码实现，包括多种解法的分析和优化。",
            "题目描述\n\n给你一个混合了数字和字母的字符串 s，其中的字母均为小写英文字母。\n\n请你将该字符串重新格式化，使得任意两个相邻的字符都不是同一类型的。也就是说，字母后面应该跟着数字，数字后面应该跟着字母。\n\n请你返回重新格式化后的字符串；如果无法按要求重新格式化，则返回一个空字符串。\n\n示例\n\n输入：s = \"a0b1c2\"\n输出：\"0a1b2c\"\n解释：\"0a1b2c\" 中任意两个相邻字符的类型都不同。 \"a0b1c2\" 也是一个可行的答案。\n\n解题思路\n\n方法一：分离后交替排列\n\n1. 将字符串中的字母和数字分别提取出来\n2. 检查两者数量差是否超过1，如果超过则无法重新格式化\n3. 让数量多的类型放在前面，交替排列\n\n代码实现\n\nJava解法：\n\npublic String reformat(String s) {\n    List<Character> letters = new ArrayList<>();\n    List<Character> digits = new ArrayList<>();\n    \n    // 分离字母和数字\n    for (char c : s.toCharArray()) {\n        if (Character.isLetter(c)) {\n            letters.add(c);\n        } else {\n            digits.add(c);\n        }\n    }\n    \n    // 检查是否可以重新格式化\n    if (Math.abs(letters.size() - digits.size()) > 1) {\n        return \"\";\n    }\n    \n    // 确定哪个类型放在前面\n    List<Character> first = letters.size() > digits.size() ? letters : digits;\n    List<Character> second = letters.size() > digits.size() ? digits : letters;\n    \n    // 交替排列\n    StringBuilder result = new StringBuilder();\n    for (int i = 0; i < first.size(); i++) {\n        result.append(first.get(i));\n        if (i < second.size()) {\n            result.append(second.get(i));\n        }\n    }\n    \n    return result.toString();\n}\n\n复杂度分析\n\n- 时间复杂度：O(n)，其中n是字符串的长度\n- 空间复杂度：O(n)，用于存储分离后的字符\n\n优化思路\n\n可以使用原地算法，直接在字符数组上操作，减少额外的空间使用。"
        );
        
        // 设置一些文章的浏览次数
        post1.setViewCount(156L);
        post2.setViewCount(89L);
        post3.setViewCount(234L);
        post4.setViewCount(67L);
        post5.setViewCount(123L);
        
        // 保存到数据库
        blogPostRepository.save(post1);
        blogPostRepository.save(post2);
        blogPostRepository.save(post3);
        blogPostRepository.save(post4);
        blogPostRepository.save(post5);
        
        System.out.println("初始化博客数据完成，共创建了5篇文章");
    }
}