-- nft_new.chat_conversations definition

CREATE TABLE `chat_conversations` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会话自增主键',
  `wallet_address_a` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参与者A的地址',
  `wallet_address_b` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参与者B的地址',
  `last_msg_id` bigint DEFAULT NULL COMMENT '本会话最后一条消息的ID（用于快速定位最新消息）',
  `last_msg_preview` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后一条消息的预览文本（截断后显示在会话列表，通常前 50~80 个字符）',
  `last_msg_time` datetime DEFAULT NULL COMMENT '最后一条消息的发送时间（用于会话列表排序）',
  `unread_a` int NOT NULL DEFAULT '0' COMMENT 'user_id_a 的未读消息数量（对方发给他的未读数）',
  `unread_b` int NOT NULL DEFAULT '0' COMMENT 'user_id_b 的未读消息数量（对方发给他的未读数）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '会话首次创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '会话最后更新时间（通常跟随最后一条消息）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pair` (`wallet_address_a`,`wallet_address_b`) COMMENT '保证两人之间只有一条会话记录',
  KEY `idx_a_last_time` (`wallet_address_a`,`last_msg_time` DESC) COMMENT '用户A的会话列表按时间倒序',
  KEY `idx_b_last_time` (`wallet_address_b`,`last_msg_time` DESC) COMMENT '用户B的会话列表按时间倒序'
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='1对1聊天会话（两人唯一会话记录，包含未读数、最后消息预览等）';


-- nft_new.chat_messages definition

CREATE TABLE `chat_messages` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息自增主键',
  `chat_conversations_id` bigint NOT NULL COMMENT '所属会话ID（外键指向 chat_conversations.id）',
  `from_address` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息发送者地址',
  `to_address` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息接收者地址（冗余字段，方便按地址查询）',
  `msg_type` tinyint NOT NULL DEFAULT '1' COMMENT '消息类型\n\n        1 = 普通文本\n\n        2 = 单张图片\n\n        3 = 多张图片（content 为 JSON 数组）\n\n        4 = NFT 卡片（展示某个 NFT 信息）\n\n        5 = 报价 / 出价（quote offer）\n\n        6 = 订单相关操作（创建、接受、拒绝、支付、完成等）\n\n        7 = 系统提示消息（已读回执、交易完成提醒等）\n\n        8 = 撤回的消息占位（未来支持撤回时使用）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息正文\n\n        - type=1：纯文本\n\n        - type=2：图片直链 或 object storage key\n\n        - type=3：JSON 数组，例如：[{"url":"...","width":1200,"height":800,"size":245760},...]\n\n        - type=4：NFT 卡片 JSON，例如：{"nft_id":12345,"name":"Azuki #8888","image_url":"...","price":1.2,"currency":"ETH"}\n\n        - type=5：报价 JSON，例如：{"amount":2.5,"currency":"ETH","expire_at":"2026-03-15T12:00:00Z","remark":"可小刀"}\n\n        - type=6：订单动作 JSON，例如：{"order_id":80001,"action":"accepted","amount":2.5}\n\n        - type=7：系统提示纯文本',
  `extra` json DEFAULT NULL COMMENT '扩展信息（备用字段，存放临时或未来新增的结构化数据）',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '接收方是否已读（0=未读，1=已读）',
  `read_at` datetime DEFAULT NULL COMMENT '对方首次阅读该消息的时间（用于“已读”显示）',
  `deleted_by_from` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发送方是否删除该消息（0=正常，1=发送方已删除）',
  `deleted_by_to` tinyint(1) NOT NULL DEFAULT '0' COMMENT '接收方是否删除该消息（0=正常，1=接收方已删除）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间（创建时间）',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '消息更新时间（更新时间）',
  PRIMARY KEY (`id`),
  KEY `idx_conv_created` (`chat_conversations_id`,`created_at` DESC) COMMENT '按会话+时间倒序查询消息（最常用索引）',
  KEY `idx_from_to_created` (`from_address`,`to_address`,`created_at` DESC) COMMENT '按两人方向查询消息（调试/审计用）',
  KEY `idx_to_unread` (`to_address`,`is_read`,`created_at`) COMMENT '统计某人未读消息（可选，视未读数实现方式）'
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='1对1私聊消息记录（支持文本、图片、NFT卡片、报价、订单关联等）';


-- nft_new.collection_stats definition

CREATE TABLE `collection_stats` (
  `collectionSlug` varchar(255) NOT NULL,
  `period` varchar(255) NOT NULL COMMENT '统计周期：24h=过去24小时，7d=过去7天，30d=过去30天，all=累计全部时间',
  `trade_volume_raw` bigint DEFAULT NULL COMMENT '该周期内该集合的总成交金额（仅 Sale 类型，以原始数值计，单位为代币最小单位）',
  `trade_count` int DEFAULT NULL COMMENT '该周期内该集合的总成交笔数（Sale + Swap 均计入）',
  `unique_traders` int DEFAULT NULL COMMENT '该周期内参与该集合交易的独立用户数（DISTINCT taker 和 lister）',
  `floor_price_raw` bigint DEFAULT NULL COMMENT '当前最低活跃 SALE 挂单价格（原始数值，需根据 coin decimals 换算展示）',
  `last_floor_update` datetime DEFAULT NULL COMMENT '最后一次更新地板价的时间（用于判断 floor_price_raw 是否新鲜）',
  `sale_count` int DEFAULT NULL COMMENT '该周期内 Sale 类型（固定价购买）的成交笔数',
  `swap_count` int DEFAULT NULL COMMENT '该周期内 Swap 类型（NFT 交换）的成交笔数',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='NFT 集合统计汇总表（用于集合排行榜、地板价展示、交易量趋势等）';


-- nft_new.fee_config_updates definition

CREATE TABLE `fee_config_updates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fee_config_object_id` varchar(255) DEFAULT NULL COMMENT '手续费配置对象的 Sui Object ID (0x...，对应 FeeConfig 的 id)',
  `old_fee_bps` int DEFAULT NULL COMMENT '变更前的费率（basis points，例如 100 = 1%）',
  `new_fee_bps` int DEFAULT NULL COMMENT '变更后的费率（basis points，例如 250 = 2.5%）',
  `old_recipient` varchar(255) DEFAULT NULL COMMENT '变更前的手续费接收地址',
  `new_recipient` varchar(255) DEFAULT NULL COMMENT '变更后的手续费接收地址',
  `tx_digest` varchar(255) DEFAULT NULL COMMENT '执行费率变更的 Sui 交易 Digest（交易哈希/标识符）',
  `timestamp_ms` bigint DEFAULT NULL COMMENT '费率变更发生的链上时间戳（毫秒，来自 tx_context::epoch_timestamp_ms）',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='手续费配置变更记录（FeeUpdated 事件对应的历史记录）';


-- nft_new.listings definition

CREATE TABLE `listings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `listing_object_id` varchar(255) DEFAULT NULL COMMENT 'OneChain Object ID (0x...)',
  `owner_address` varchar(255) DEFAULT NULL COMMENT '挂单人地址',
  `nft_object_id` varchar(255) DEFAULT NULL COMMENT '挂单中的 NFT Object ID',
  `nft_type` varchar(255) DEFAULT NULL COMMENT 'NFT 类型全名 (type_name::with_original_ids<T>())',
  `listing_type` int DEFAULT NULL COMMENT '0=SWAP, 1=SALE',
  `status` int DEFAULT NULL COMMENT '0=ACTIVE, 1=FILLED, 2=CANCELLED',
  `expected_nft_type` varchar(255) DEFAULT NULL COMMENT 'Swap 时期望的 NFT 类型',
  `price` bigint DEFAULT NULL COMMENT 'SALE 时的价格（原始数值，需注意 decimals）',
  `coin_type` varchar(255) DEFAULT NULL COMMENT 'SALE 时的支付币种类型',
  `collection_slug` varchar(255) DEFAULT NULL COMMENT 'NFT 集合标识（链下映射或从元数据获取）',
  `collection_name` varchar(255) DEFAULT NULL COMMENT '集合名称（可选，便于展示）',
  `create_tx_digest` varchar(255) DEFAULT NULL COMMENT '创建交易 digest',
  `filled_tx_digest` varchar(255) DEFAULT NULL COMMENT '挂单被成交 digest',
  `filled_timestamp_ms` bigint DEFAULT NULL COMMENT '挂单被成交时的链上时间戳',
  `cancel_tx_digest` varchar(255) DEFAULT NULL COMMENT '挂单被取消 digest',
  `cancel_timestamp_ms` bigint DEFAULT NULL COMMENT '挂单被取消时的链上时间戳',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `listings_unique` (`listing_object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='NFT 挂单表';


-- nft_new.nft_delist_event definition

CREATE TABLE `nft_delist_event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `wallet_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '钱包地址',
  `nft_object_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'nft对象id',
  `listing_object_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '市场id',
  `tx_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'hash',
  `event_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事件type',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='下架事件表';


-- nft_new.nft_listing_event definition

CREATE TABLE `nft_listing_event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `wallet_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '钱包地址',
  `nft_object_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'nft对象id',
  `listing_object_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上架id',
  `tx_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'hash',
  `event_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事件type',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `listing_price` bigint DEFAULT NULL COMMENT '上架价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='上架事件表';


-- nft_new.nft_sale_filled_event definition

CREATE TABLE `nft_sale_filled_event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nft_object_id` varchar(255) DEFAULT NULL COMMENT 'nft对象id',
  `listing_object_id` varchar(255) DEFAULT NULL COMMENT '市场id',
  `taker` varchar(255) DEFAULT NULL COMMENT '买家地址',
  `lister` varchar(255) DEFAULT NULL COMMENT '卖家（挂单人）地址',
  `payment_amount` bigint DEFAULT '0' COMMENT '买家实际支付的 Coin 数量（原始 value）',
  `seller_amount` bigint DEFAULT '0' COMMENT '卖家实际收到金额',
  `fee_amount` bigint DEFAULT '0' COMMENT '实际扣除的手续费',
  `coin_type` varchar(255) DEFAULT NULL COMMENT '支付的币种',
  `tx_hash` varchar(255) DEFAULT NULL COMMENT 'hash',
  `event_type` varchar(255) DEFAULT NULL COMMENT '事件type',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nft_sale_filled_event_unique` (`tx_hash`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='售卖事件表';


-- nft_new.nft_swap_filled_event definition

CREATE TABLE `nft_swap_filled_event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nft_object_id` varchar(255) DEFAULT NULL COMMENT 'nft对象id',
  `listing_object_id` varchar(255) DEFAULT NULL COMMENT '市场id',
  `taker` varchar(255) DEFAULT NULL COMMENT '买家地址',
  `lister` varchar(255) DEFAULT NULL COMMENT '卖家（挂单人）地址',
  `nft_object_id_out` varchar(255) DEFAULT NULL COMMENT 'listing 中原有的 NFT（给 taker）',
  `nft_object_id_in` varchar(255) DEFAULT NULL COMMENT 'taker 提供的换入 NFT（给 lister）',
  `tx_hash` varchar(255) DEFAULT NULL COMMENT 'hash',
  `event_type` varchar(255) DEFAULT NULL COMMENT '事件type',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nft_swap_filled_event_unique` (`tx_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交换事件表';


-- nft_new.notifications definition

CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知类型，如 NFT_SOLD / USER_FOLLOWED / SYSTEM_ANNOUNCEMENT',
  `source_type` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知来源类型：USER（用户行为）或 SYSTEM（系统通知）',
  `actor_address` varchar(66) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '触发通知的用户地址（系统通知可为空）',
  `target_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知关联对象类型：NFT / LISTING / USER',
  `target_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知关联对象ID，如 nft_id / listing_id / user_address',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知标题（前端可直接展示）',
  `content` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知正文内容',
  `metadata` json DEFAULT NULL COMMENT '扩展信息JSON，如价格、币种等附加数据',
  `priority` tinyint NOT NULL DEFAULT '0' COMMENT '优先级：0普通 1重要 2紧急',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_priority` (`priority`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知主表：定义通知内容与基础信息';


-- nft_new.platform_stats_daily definition

CREATE TABLE `platform_stats_daily` (
  `statDate` varchar(255) NOT NULL COMMENT '统计日期 (YYYY-MM-DD)',
  `trade_volume_raw` bigint DEFAULT NULL COMMENT '所有 Sale payment_amount 之和',
  `trade_count` int DEFAULT NULL COMMENT '累计交易',
  `unique_buyers` int DEFAULT NULL COMMENT 'DISTINCT taker',
  `unique_sellers` int DEFAULT NULL COMMENT 'DISTINCT lister',
  `fee_earned_raw` bigint DEFAULT NULL COMMENT '累计手续费',
  `swap_count` int DEFAULT NULL COMMENT '累计swap单数',
  `sale_count` int DEFAULT NULL COMMENT '累计sale单数',
  `active_listings` int DEFAULT NULL COMMENT '当前活跃挂单数 (需定时更新)',
  `created_listings` int DEFAULT NULL COMMENT '当天新增挂单',
  `cancelled_listings` int DEFAULT NULL COMMENT '当天取消挂单',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`statDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台每日统计快照';


-- nft_new.processed_events definition

CREATE TABLE `processed_events` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tx_hash` varchar(255) DEFAULT NULL COMMENT 'hash',
  `event_type` varchar(255) DEFAULT NULL COMMENT '事件类型',
  `processed_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='已处理事件表';


-- nft_new.trades definition

CREATE TABLE `trades` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `listing_object_id` varchar(255) DEFAULT NULL,
  `trade_type` varchar(255) DEFAULT NULL COMMENT '1=Sale (买NFT), 2=Swap (NFT↔NFT)',
  `taker_address` varchar(255) DEFAULT NULL COMMENT '主动成交方 (买家/提供换入NFT的人)',
  `lister_address` varchar(255) DEFAULT NULL COMMENT '挂单人 (卖家/原NFT持有者)',
  `nft_object_id_out` varchar(255) DEFAULT NULL COMMENT '给 taker 的 NFT (原挂单的)',
  `nft_object_id_in` varchar(255) DEFAULT NULL COMMENT '给 lister 的 NFT (Swap 时才有)',
  `payment_amount` bigint DEFAULT NULL COMMENT 'Sale 时的支付金额 (原始值)',
  `fee_amount` bigint DEFAULT NULL COMMENT 'Sale 时的手续费',
  `seller_amount` bigint DEFAULT NULL COMMENT 'Sale 时代理收到金额',
  `coin_type` varchar(255) DEFAULT NULL COMMENT 'Sale 时的支付币种类型',
  `collection_slug` varchar(255) DEFAULT NULL COMMENT '集合标识 (方便聚合)',
  `tx_digest` varchar(255) DEFAULT NULL COMMENT '成交交易 digest',
  `timestamp_ms` bigint DEFAULT NULL COMMENT '成交时间戳 (ms)',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='NFT 成交记录表';


-- nft_new.user_activities definition

CREATE TABLE `user_activities` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'activity id',
  `actor_address` varchar(66) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行为发起者 address',
  `activity_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行为类型',
  `target_type` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标类型: NFT | LISTING | USER',
  `target_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标 ID（nft_id / listing_id / user address）',
  `metadata` json DEFAULT NULL COMMENT '行为附加信息',
  `tx_digest` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '链上交易 digest（仅链上行为）',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_actor_time` (`actor_address`,`created_at` DESC),
  KEY `idx_target` (`target_type`,`target_id`),
  KEY `idx_activity_type` (`activity_type`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户活动记录表';


-- nft_new.user_activity_stats definition

CREATE TABLE `user_activity_stats` (
  `address` varchar(255) NOT NULL COMMENT '用户钱包地址（Sui 地址，0x...，作为主键）',
  `total_trades` int DEFAULT NULL COMMENT '该用户累计参与的成交次数（Sale + Swap 均计入）',
  `total_volume_raw` bigint DEFAULT NULL COMMENT '该用户累计成交金额总和（仅 Sale 类型，以原始数值计，单位为代币最小单位）',
  `buy_volume_raw` bigint DEFAULT NULL COMMENT '该用户作为买家（taker）的累计成交金额（Sale 类型，支付金额）',
  `sell_volume_raw` bigint DEFAULT NULL COMMENT '该用户作为卖家（lister）的累计成交金额（Sale 类型，收到金额，不含手续费）',
  `fee_paid_raw` bigint DEFAULT NULL COMMENT '该用户累计支付的手续费总额（Sale 类型中作为买家支付的部分）',
  `last_active` datetime DEFAULT NULL COMMENT '该用户最后一次参与交易（成交）的时间（后端记录，基于链上 timestamp_ms）',
  `rank_volume` int DEFAULT NULL COMMENT '按 total_volume_raw 降序排名的名次（可通过定时任务计算并更新）',
  `rank_trades` int DEFAULT NULL COMMENT '按 total_trades 降序排名的名次（可通过定时任务计算并更新）',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户交易活动汇总表（用于用户排行榜、个人交易面板、鲸鱼榜等）';


-- nft_new.user_collections definition

CREATE TABLE `user_collections` (
  `id` int NOT NULL AUTO_INCREMENT,
  `wallet_address` varchar(128) NOT NULL COMMENT '用户id',
  `nft_object_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'nft对象id',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_collections_unique` (`wallet_address`,`nft_object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';


-- nft_new.user_follows definition

CREATE TABLE `user_follows` (
  `id` int NOT NULL AUTO_INCREMENT,
  `follower_wallet_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户地址',
  `following_wallet_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '被关注用户地址',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注表';


-- nft_new.users definition

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `wallet_address` varchar(255) DEFAULT NULL COMMENT '钱包地址',
  `email` varchar(255) DEFAULT NULL COMMENT 'email',
  `last_signed_in` datetime DEFAULT NULL COMMENT '上次登录时间',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `description` text COMMENT '个人简介',
  `twitter` varchar(255) DEFAULT NULL COMMENT 'twitter',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';


-- nft_new.verification_applications definition

CREATE TABLE `verification_applications` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，自增ID',
  `applicant_wallet` varchar(66) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请者钱包地址，格式 0x...（64 hex）',
  `collection_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合类型标识，Move 类型如 0x...::module::Name',
  `collection_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合展示名称',
  `logo_url` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合 logo 图片 URL（可为 CDN/IPFS）',
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合描述',
  `website_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '官网或项目页面 URL',
  `social_links` json NOT NULL COMMENT '社交链接 JSON，如 {"twitter":"", "discord":""}',
  `unique_holders_count` int NOT NULL COMMENT '唯一持有者数量（>=0）',
  `is_original` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否原创项目（申请者声明）',
  `proof_materials` text COLLATE utf8mb4_unicode_ci COMMENT '证明材料，可包含图片/文档 URL 列表或说明',
  `contact_email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系人邮箱',
  `agreed_terms` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否同意条款（必选）',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '申请状态，例如 pending/approved/rejected',
  `rejection_reason` text COLLATE utf8mb4_unicode_ci COMMENT '若被拒绝，填写拒绝原因',
  `reviewed_at` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_display` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否符合display标准',
  `category` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类：PFP/ART/GAMING/COLLECTIBLE/METAVERSE/MUSIC/FASHION/DOMAIN/UTILITY/MEME/RWA/OTHER',
  PRIMARY KEY (`id`),
  KEY `idx_verif_app_collection_type` (`collection_type`) COMMENT '按集合类型索引，用于查询申请记录',
  KEY `idx_verif_app_status` (`status`) COMMENT '按状态索引，用于筛选待审核/已处理申请',
  CONSTRAINT `chk_applicant_wallet_regexp` CHECK (regexp_like(`applicant_wallet`,_utf8mb4'^0x[a-fA-F0-9]{64}$')),
  CONSTRAINT `verification_applications_chk_1` CHECK ((`unique_holders_count` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='NFT 集合验证申请记录表，保存申请者提交的材料与状态';


-- nft_new.collections_verification definition

CREATE TABLE `collections_verification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，自增ID',
  `collection_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合类型标识，唯一（如 Move type）',
  `is_verified` tinyint(1) NOT NULL DEFAULT '0' COMMENT '集合是否已验证',
  `verified_at` timestamp NULL DEFAULT NULL COMMENT '验证通过时间',
  `last_application_id` bigint DEFAULT NULL COMMENT '最近一次关联的申请记录 ID',
  `revoked_at` timestamp NULL DEFAULT NULL COMMENT '若被撤销，记录撤销时间',
  `revoke_reason` text COLLATE utf8mb4_unicode_ci COMMENT '撤销原因说明',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类：PFP/ART/GAMING/COLLECTIBLE/METAVERSE/MUSIC/FASHION/DOMAIN/UTILITY/MEME/RWA/OTHER',
  `collection_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合展示名称',
  `logo_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合 logo 图片 URL（可为 CDN/IPFS）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集合描述',
  `website_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '官网或项目页面 URL',
  `social_links` json NOT NULL COMMENT '社交链接 JSON，如 {"twitter":"", "discord":""}',
  `unique_holders_count` int NOT NULL COMMENT '唯一持有者数量（>=0）',
  `is_original` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否原创项目（申请者声明）',
  `proof_materials` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '证明材料，可包含图片/文档 URL 列表或说明',
  `contact_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系人邮箱',
  `agreed_terms` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否同意条款（必选）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `collection_type` (`collection_type`),
  KEY `fk_last_application` (`last_application_id`),
  KEY `idx_col_verif_type_verified` (`collection_type`,`is_verified`) COMMENT '按集合类型与是否验证索引，用于快速查询验证状态',
  CONSTRAINT `fk_last_application` FOREIGN KEY (`last_application_id`) REFERENCES `verification_applications` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='集合验证状态表，记录每个集合的验证结果与历史关联申请';


-- nft_new.user_notifications definition

CREATE TABLE `user_notifications` (
  `notification_id` int NOT NULL COMMENT '通知ID，对应 notifications.id',
  `recipient_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接收通知的用户地址',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读：0未读 1已读',
  `read_at` timestamp NULL DEFAULT NULL COMMENT '已读时间',
  PRIMARY KEY (`notification_id`,`recipient_address`),
  KEY `idx_user_unread` (`recipient_address`,`is_read`),
  KEY `idx_user_time` (`recipient_address`,`notification_id`),
  CONSTRAINT `fk_notification` FOREIGN KEY (`notification_id`) REFERENCES `notifications` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户通知关系表：记录通知接收人与已读状态';