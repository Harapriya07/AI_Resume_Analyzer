// Mock API Service for Resume Analysis
// In production, these would connect to a real backend

const MOCK_ATS_SCORES = [45, 58, 72, 68, 85, 92, 76, 55, 88, 79];
const MOCK_SKILLS = [
  { name: 'JavaScript', match: 95, category: 'Programming' },
  { name: 'React', match: 90, category: 'Framework' },
  { name: 'Node.js', match: 85, category: 'Runtime' },
  { name: 'Python', match: 80, category: 'Programming' },
  { name: 'SQL', match: 88, category: 'Database' },
  { name: 'Git', match: 92, category: 'Tools' },
  { name: 'CSS/HTML', match: 95, category: 'Frontend' },
  { name: 'REST APIs', match: 87, category: 'Architecture' },
];

const MOCK_FEEDBACK = [
  '✓ Strong technical background detected',
  '✓ Good project experience mentioned',
  '⚠ Consider adding more quantifiable achievements',
  '⚠ Add certifications section for better ATS recognition',
  '✓ Clear work history and progression',
  '⚠ Customize resume for each job application',
];

const MOCK_JOB_MATCHES = [
  {
    id: 1,
    title: 'Senior Frontend Developer',
    company: 'TechCorp Inc',
    matchPercentage: 92,
    salary: '$120K - $160K',
    skills: ['JavaScript', 'React', 'CSS/HTML'],
  },
  {
    id: 2,
    title: 'Full Stack Developer',
    company: 'StartUp Labs',
    matchPercentage: 88,
    salary: '$100K - $140K',
    skills: ['JavaScript', 'Node.js', 'React', 'SQL'],
  },
  {
    id: 3,
    title: 'Backend Engineer',
    company: 'CloudServices Ltd',
    matchPercentage: 85,
    salary: '$110K - $150K',
    skills: ['Node.js', 'Python', 'SQL', 'REST APIs'],
  },
];

// Simulate API delay
const delay = (ms = 500) => new Promise(resolve => setTimeout(resolve, ms));

export const analyzeResume = async (file) => {
  await delay(1000);
  
  const randomScore = MOCK_ATS_SCORES[Math.floor(Math.random() * MOCK_ATS_SCORES.length)];
  
  return {
    fileName: file.name,
    uploadedAt: new Date().toLocaleDateString(),
    atsScore: randomScore,
    scorePercentage: randomScore,
    rating: randomScore >= 80 ? 'Excellent' : randomScore >= 60 ? 'Good' : 'Fair',
    skills: MOCK_SKILLS.sort(() => Math.random() - 0.5).slice(0, 6),
    feedback: MOCK_FEEDBACK,
    jobMatches: MOCK_JOB_MATCHES,
  };
};

export const getSkillsAnalysis = async () => {
  await delay(500);
  return MOCK_SKILLS;
};

export const getJobMatches = async () => {
  await delay(800);
  return MOCK_JOB_MATCHES;
};

export const getFeedback = async () => {
  await delay(600);
  return MOCK_FEEDBACK;
};